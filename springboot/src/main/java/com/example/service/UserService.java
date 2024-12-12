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
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    @Resource
    private UserDao userDao;

    public User add(User user) {
        // 唯一校验
        User info = userDao.findByUsername(user.getUsername());
        if (ObjectUtil.isNotEmpty(info)) {
            throw new CustomException(ResultCode.USER_EXIST_ERROR);
        }
        if (StringUtils.isEmpty(user.getPassword())) {
            // 默认密码123456
            user.setPassword("123456");
        }
        if (StringUtils.isEmpty(user.getAvatar())) {
            user.setAvatar("http://localhost:8080/files/qy-default.png");
        }
        userDao.insertSelective(user);
        return user;
    }

    public void delete(Integer id) {
        userDao.deleteByPrimaryKey(id);
    }

    public void update(User user) {
        userDao.updateByPrimaryKeySelective(user);
    }

    public User findById(Integer id) {
        return userDao.selectByPrimaryKey(id);
    }

    public List<User> findAll() {
        return userDao.findBySearch(null);
    }

    public PageInfo<User> findPage(User search, Integer pageNum, Integer pageSize) {
        Account user = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isEmpty(user)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<User> all = findByCondition(search);

        return PageInfo.of(all);
    }

    public List<User> findByCondition(User search) {
		return userDao.findBySearch(search);
    }

    public User findByUserName(String username) {
        return userDao.findByUsername(username);
    }

    public User login(String username, String mm) {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new CustomException(ResultCode.USER_ACCOUNT_ERROR);
        }
        if (!mm.equalsIgnoreCase(user.getPassword())) {
            throw new CustomException(ResultCode.USER_ACCOUNT_ERROR);
        }
        return user;
    }

}
