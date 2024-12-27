package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.config.JwtTokenUtils;
import com.example.common.ResultCode;
import com.example.dao.*;
import com.example.entity.*;
import com.example.exception.CustomException;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsService {

    @Resource
    private GoodsDao goodsDao;

    public Goods add(Goods goods) {
        goodsDao.insertSelective(goods);
        return goods;
    }

    public void delete(Integer id) {
        goodsDao.deleteByPrimaryKey(id);
    }

    public void update(Goods goods) {
        goodsDao.updateByPrimaryKeySelective(goods);
    }

    public Goods findById(Integer id) {
        return goodsDao.selectByPrimaryKey(id);
    }

    public List<Goods> findAll() {
        return goodsDao.findBySearch(null);
    }

    public PageInfo<Goods> findPage(Goods search, Integer pageNum, Integer pageSize) {
        Account user = getAccount();
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> all = findByCondition(user, search);

        return PageInfo.of(all);
    }
    public PageInfo<Goods> findHotPage(Goods search, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> byHot = goodsDao.findByHot();
        return PageInfo.of(byHot);
    }

    public List<Goods> findByCondition(Account user, Goods search) {
		return goodsDao.findBySearch(search);
    }

    public List<Goods> findByFlId(Integer id) {
        return goodsDao.findByTypeId(id);
    }

    private Account getAccount() {
        Account user = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isNull(user)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }
        return user;
    }

    public List<Goods> search(String name) {
        return goodsDao.findByName(name);
    }
}