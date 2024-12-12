package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.ResultCode;
import com.example.common.config.JwtTokenUtils;
import com.example.dao.BannerDao;
import com.example.entity.Account;
import com.example.entity.Banner;
import com.example.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BannerService {

    @Resource
    private BannerDao bannerDao;


    public Banner add(Banner banner) {
        bannerDao.insertSelective(banner);
        return banner;
    }

    public void delete(Integer id) {
        bannerDao.deleteByPrimaryKey(id);
    }

    public void update(Banner banner) {
        bannerDao.updateByPrimaryKeySelective(banner);
    }

    public Banner findById(Integer id) {
        return bannerDao.selectByPrimaryKey(id);
    }

    public List<Banner> findAll() {
        return bannerDao.findBySearch(null);
    }

    public PageInfo<Banner> findPage(Banner search, Integer pageNum, Integer pageSize) {
        Account user = getAccount();
        PageHelper.startPage(pageNum, pageSize);
        List<Banner> all = findByCondition(user, search);

        return PageInfo.of(all);
    }


    public List<Banner> findByCondition(Account user, Banner search) {
		return bannerDao.findBySearch(search);
    }




    private Account getAccount() {
        Account user = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isNull(user)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }
        return user;
    }

}