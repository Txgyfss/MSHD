package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.ResultCode;
import com.example.common.config.JwtTokenUtils;
import com.example.dao.AddressDao;
import com.example.entity.Account;
import com.example.entity.Address;
import com.example.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AddressService {

    @Resource
    private AddressDao addressDao;

    public Address add(Address address) {
        addressDao.insertSelective(address);
        return address;
    }

    public void delete(Integer id) {
        addressDao.deleteByPrimaryKey(id);
    }

    public List<Address> findAll() {
        Account currentUser = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isNull(currentUser)) {
            throw new CustomException("-1", "登录失效，请重新登录");
        }
        return addressDao.findByUserIdAndRole(currentUser.getId(), currentUser.getRole());
    }

    public PageInfo<Address> findPage(Address search, Integer pageNum, Integer pageSize) {
        Account currentUser = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isNull(currentUser)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Address> all = findByCondition(search, currentUser);
        return PageInfo.of(all);
    }

    public List<Address> findByCondition(Address search, Account currentUser) {
        if (1 == currentUser.getRole()) {
            return addressDao.findBySearch(search, null, null);
        } else {
            return addressDao.findBySearch(search, currentUser.getId(), currentUser.getRole());
        }
    }
}