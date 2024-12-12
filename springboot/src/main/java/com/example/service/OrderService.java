package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.dao.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.example.common.ResultCode;
import com.example.common.config.JwtTokenUtils;
import com.example.entity.*;
import com.example.exception.CustomException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {


    @Resource
    private OrderDao orderDao;
    @Resource
    private GoodsDao goodsDao;
    @Resource
    private CartDao cartDao;
    @Resource
    private UserDao userDao;
    @Resource
    private AddressDao addressDao;

    public void add(OrderDto orderDto) {
        List<Cart> list = orderDto.getList();
        Integer addressId = orderDto.getAddressId();
        Account currentUser = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isNull(currentUser)) {
            throw new CustomException("-1", "登录失效，请重新登录");
        }
        // 计算总价格
        Double totalPrice = calculateTotalPrice(list);
        User user = userDao.selectByPrimaryKey(currentUser.getId());
        if (user.getAccount() < totalPrice) {
            throw new CustomException("-1", "您的余额不足，请到个人中心充值");
        }
        for (Cart cart : list) {
            Order order = new Order();
            order.setOrderId(DateUtil.format(new Date(), "yyyyMMddhhmmss"));
            order.setTime(DateUtil.now());
            order.setUserId(currentUser.getId());

            Goods goods = goodsDao.selectByPrimaryKey(cart.getGoodsId());
            order.setGoodsId(goods.getId());
            order.setNum(cart.getNum());
            order.setPrice(goods.getPrice() * cart.getNum());
            order.setStatus("待发货");
            order.setAddressId(addressId);

            orderDao.insertSelective(order);

            //减库存
            Integer num = cart.getNum();
            Integer goodsnum = goods.getGoodsnum();
            goods.setGoodsnum(goodsnum-num);
            goodsDao.updateByPrimaryKeySelective(goods);

            // 删除购物车中该数据
            cartDao.deleteByPrimaryKey(cart.getId());
        }
//        减余额
        Double account = user.getAccount();
        user.setAccount(account-totalPrice);
        userDao.updateByPrimaryKeySelective(user);
    }

    private Double calculateTotalPrice(List<Cart> list) {
        double total = 0;
        for (Cart cart : list) {
            total += cart.getPrice() * cart.getNum();
        }
        return total;
    }

    public void delete(Integer id) {
        orderDao.deleteByPrimaryKey(id);
    }

    public void update(Order order) {
        //如果当前状态是退款申请中 更新状态是退款完成 退款给用户。
        Integer id = order.getId();
        Order ordernow = orderDao.selectByPrimaryKey(id);
        String status = ordernow.getStatus();
        if (("退款申请中".equals(status)) && ("退款完成".equals(order.getStatus()))){
            Double price = order.getPrice();
            Integer userId = order.getUserId();
            User user = userDao.selectByPrimaryKey(userId);
            //退款
            if (ObjectUtil.isNotNull(user)){
                Double account = user.getAccount();
                user.setAccount(account + price);
                userDao.updateByPrimaryKeySelective(user);
            }
            //退库存
            Integer goodsId = order.getGoodsId();
            Goods goods = goodsDao.selectByPrimaryKey(goodsId);
            if (ObjectUtil.isNotNull(goods)){
                Integer goodsnum = goods.getGoodsnum();
                goods.setGoodsnum(goodsnum + order.getNum());
                goodsDao.updateByPrimaryKeySelective(goods);
            }
        }

        orderDao.updateByPrimaryKeySelective(order);
    }
    public void addOne(Order order) {
        Account currentUser = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isNull(currentUser)) {
            throw new CustomException("-1", "登录失效，请重新登录");
        }
        User user = userDao.selectByPrimaryKey(currentUser.getId());
        if (user.getAccount() < order.getPrice()) {
            throw new CustomException("-1", "您的余额不足，请到个人中心充值");
        }
        order.setOrderId(DateUtil.format(new Date(), "yyyyMMddhhmmss"));
        order.setTime(DateUtil.now());
        order.setStatus("待发货");
        orderDao.insertSelective(order);
        Double account = user.getAccount();
        user.setAccount(account - order.getPrice());
        userDao.updateByPrimaryKeySelective(user);
    }
    public Order findById(Integer id) {
        return orderDao.selectByPrimaryKey(id);
    }

    public List<Order> findAll() {
        Account currentUser = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isNull(currentUser)) {
            throw new CustomException("-1", "登录失效，请重新登录");
        }
        List<Order> list = orderDao.findByUserId(currentUser.getId());
        extracted(list);
        return list;
    }

    public PageInfo<Order> findPage(Order search, Integer pageNum, Integer pageSize) {
        Account user = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isNull(user)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Order> all = findByCondition(user, search);
        for (Order order : all) {
            Address address = addressDao.selectByPrimaryKey(order.getAddressId());
            if (ObjectUtil.isNotEmpty(address)){
                order.setName(address.getUsername());
                order.setAddress(address.getAddress());
                order.setPhone(address.getPhone());
            }
        }
        return PageInfo.of(all);
    }

    public List<Order> findByCondition(Account user, Order search) {
        List<Order> list = orderDao.findBySearch(search);
        extracted(list);
        return list;
    }

    private void extracted(List<Order> list) {
        for (Order order : list) {
            User user = userDao.selectByPrimaryKey(order.getUserId());
            if (ObjectUtil.isNotEmpty(user)){
                order.setUserName(user.getUsername());
            }
            // 处理订单中的商品
            Goods goods = goodsDao.selectByPrimaryKey(order.getGoodsId());
            if (ObjectUtil.isNotEmpty(goods)){
                goods.setNum(order.getNum());
                order.setGoods(goods);
            }
            // 处理收货
            Address address = addressDao.selectByPrimaryKey(order.getAddressId());
            if (ObjectUtil.isNotEmpty(address)){
                order.setName(address.getUsername());
                order.setAddress(address.getAddress());
                order.setPhone(address.getPhone());
            }
        }
    }
}