package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.example.common.ResultCode;
import com.example.common.config.JwtTokenUtils;
import com.example.dao.CartDao;
import com.example.dao.GoodsDao;
import com.example.entity.Account;
import com.example.entity.Cart;
import com.example.entity.Goods;
import com.example.exception.CustomException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CartService {

    @Resource
    private CartDao cartDao;
    @Resource
    private GoodsDao goodsDao;

    public Cart add(Cart cart) {
        cartDao.insertSelective(cart);
        return cart;
    }

    public void delete(Integer id) {
        cartDao.deleteByPrimaryKey(id);
    }

    public void update(Cart cart) {
        cartDao.updateByPrimaryKeySelective(cart);
    }

    public Cart findById(Integer id) {
        return cartDao.selectByPrimaryKey(id);
    }

    public List<Cart> findAll() {
        Account currentUser = JwtTokenUtils.getCurrentUser();
        List<Cart> list = cartDao.findByUserId(currentUser.getId());
        for (Cart cart : list) {
            Goods goods = goodsDao.selectByPrimaryKey(cart.getGoodsId());
            cart.setImg(goods.getImg());
            cart.setGoodsName(goods.getName());
            cart.setPrice(goods.getPrice());
        }
        return list;
    }

    public PageInfo<Cart> findPage(Cart search, Integer pageNum, Integer pageSize) {
        Account user = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isNull(user)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Cart> all = findByCondition(user, search);

        return PageInfo.of(all);
    }

    public List<Cart> findByCondition(Account user, Cart search) {
		return cartDao.findBySearch(search);
    }
}