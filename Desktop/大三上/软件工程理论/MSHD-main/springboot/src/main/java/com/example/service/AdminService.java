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
public class AdminService {

    @Resource
    private AdminDao adminDao;

    public Admin add(Admin admin) {
        // 唯一校验
        Admin info = adminDao.findByUsername(admin.getUsername());
        if (ObjectUtil.isNotEmpty(info)) {
            throw new CustomException(ResultCode.USER_EXIST_ERROR);
        }
        if (StringUtils.isEmpty(admin.getPassword())) {
            // 默认密码123456
            admin.setPassword("123456");
        }
        if (StringUtils.isEmpty(admin.getAvatar())) {
            admin.setAvatar("http://localhost:8080/files/qy-default.png");
        }
        adminDao.insertSelective(admin);
        return admin;
    }

    public void delete(Integer id) {
        adminDao.deleteByPrimaryKey(id);
    }

    public void update(Admin admin) {
        adminDao.updateByPrimaryKeySelective(admin);
    }

    public Admin findById(Integer id) {
        return adminDao.selectByPrimaryKey(id);
    }

    public List<Admin> findAll() {
        return adminDao.findBySearch(null);
    }

    public PageInfo<Admin> findPage(Admin search, Integer pageNum, Integer pageSize) {
        Account user = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isEmpty(user)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Admin> all = findByCondition(search);

        return PageInfo.of(all);
    }

    public List<Admin> findByCondition(Admin search) {
		return adminDao.findBySearch(search);
    }

    public Admin findByUsername(String username) {
        return adminDao.findByUsername(username);
    }

    public Admin login(String username, String mm) {
        Admin admin = adminDao.findByUsername(username);
        if (admin == null) {
            throw new CustomException(ResultCode.USER_ACCOUNT_ERROR);
        }
        if (!mm.equalsIgnoreCase(admin.getPassword())) {
            throw new CustomException(ResultCode.USER_ACCOUNT_ERROR);
        }
        return admin;
    }

}
