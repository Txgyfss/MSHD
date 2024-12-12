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

@Service
public class TypeService {

    @Resource
    private TypeDao typeDao;

    public Type add(Type type) {
        typeDao.insertSelective(type);
        return type;
    }

    public void delete(Integer id) {
        typeDao.deleteByPrimaryKey(id);
    }

    public void update(Type type) {
        typeDao.updateByPrimaryKeySelective(type);
    }

    public Type findById(Integer id) {
        return typeDao.selectByPrimaryKey(id);
    }

    public List<Type> findAll() {
        return typeDao.findBySearch(null);
    }

    public PageInfo<Type> findPage(Type search, Integer pageNum, Integer pageSize) {
        Account user = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isNull(user)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Type> all = findByCondition(user, search);

        return PageInfo.of(all);
    }

    public List<Type> findByCondition(Account user, Type search) {
		return typeDao.findBySearch(search);
    }
}