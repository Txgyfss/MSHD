package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.common.ResultCode;
import com.example.common.config.JwtTokenUtils;
import com.example.dao.NoticeDao;
import com.example.entity.Account;
import com.example.entity.Notice;
import com.example.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class NoticeService {

    @Resource
    private NoticeDao noticeDao;

    public Notice add(Notice notice) {
        notice.setTime(DateUtil.now());
        noticeDao.insertSelective(notice);
        return notice;
    }

    public void delete(Integer id) {
        noticeDao.deleteByPrimaryKey(id);
    }

    public void update(Notice notice) {
        noticeDao.updateByPrimaryKeySelective(notice);
    }

    public List<Notice> findAll() {
        return noticeDao.findTop3();
    }

    public PageInfo<Notice> findPage(Notice search, Integer pageNum, Integer pageSize) {
        Account user = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isNull(user)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Notice> all = findByCondition(user, search);

        return PageInfo.of(all);
    }

    public List<Notice> findByCondition(Account user, Notice search) {
		return noticeDao.findBySearch(search);
    }
}