package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.ResultCode;
import com.example.common.config.JwtTokenUtils;
import com.example.dao.AdvertisingDao;
import com.example.entity.Advertising;
import com.example.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import com.example.entity.Account;
import javax.annotation.Resource;
import java.util.List;

@Service
public class AdvertisingService {

    @Resource
    private AdvertisingDao advertisingDao;


    public Advertising add(Advertising advertising) {
        advertisingDao.insertSelective(advertising);
        return advertising;
    }

    public void delete(Integer id) {
        advertisingDao.deleteByPrimaryKey(id);
    }

    public void update(Advertising advertising) {
        advertisingDao.updateByPrimaryKeySelective(advertising);
    }

    public Advertising findById(Integer id) {
        return advertisingDao.selectByPrimaryKey(id);
    }

    public List<Advertising> findAll() {
        return advertisingDao.findBySearch(null);
    }

    public PageInfo<Advertising> findPage(Advertising search, Integer pageNum, Integer pageSize) {
        Account user = getAccount();
        PageHelper.startPage(pageNum, pageSize);
        List<Advertising> all = findByCondition(user, search);

        return PageInfo.of(all);
    }


    public List<Advertising> findByCondition(Account user, Advertising search) {
		return advertisingDao.findBySearch(search);
    }




    private Account getAccount() {
        Account user = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isNull(user)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }
        return user;
    }

}