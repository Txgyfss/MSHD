package com.example.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.example.common.ResultCode;
import com.example.common.config.JwtTokenUtils;
import com.example.dao.CommentDao;
import com.example.dao.AdminDao;
import com.example.dao.UserDao;
import com.example.entity.*;
import com.example.exception.CustomException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CommentService {

    @Resource
    private CommentDao commentDao;
    @Resource
    private AdminDao adminDao;
    @Resource
    private UserDao userDao;

    public Comment add(Comment comment) {
        comment.setTime(DateUtil.now());
        commentDao.insertSelective(comment);
        return comment;
    }

    public void delete(Integer id) {
        commentDao.deleteByPrimaryKey(id);
    }

    public List<Comment> findAll(Integer goodsId) {
        Account currentUser = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isNull(currentUser)) {
            throw new CustomException("-1", "登录失效，请重新登录");
        }
        List<Comment> list = commentDao.findByGoodsIdAndParentId(goodsId, 0);
        for (Comment comment : list) {
            setData(comment);
            List<Comment> children = commentDao.findByGoodsIdAndParentId(goodsId, comment.getId());
            if (CollectionUtil.isNotEmpty(children)) {
                for (Comment child : children) {
                    setData(child);
                }
                comment.setChildren(children);
            }
        }
        return list;
    }

    private void setData(Comment comment) {
        if (1 == comment.getRole()) {
            Admin admin = adminDao.selectByPrimaryKey(comment.getUserId());
            comment.setUserName(admin.getUsername());
            comment.setAvatar(admin.getAvatar());
        }
        if (2 == comment.getRole()) {
            User user = userDao.selectByPrimaryKey(comment.getUserId());
            comment.setUserName(user.getUsername());
            comment.setAvatar(user.getAvatar());
        }
    }

    public PageInfo<Comment> findPage(Comment search, Integer pageNum, Integer pageSize) {
        Account currentUser = JwtTokenUtils.getCurrentUser();
        if (ObjectUtil.isNull(currentUser)) {
            throw new CustomException(ResultCode.USER_NOT_LOGIN);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Comment> all = findByCondition(search, currentUser);
        for (Comment comment : all) {
            Integer role = comment.getRole();
            if (1 == role) {
                Admin admin = adminDao.selectByPrimaryKey(comment.getUserId());
                comment.setUserName(admin.getUsername());
            } else {
                User user = userDao.selectByPrimaryKey(comment.getUserId());
                comment.setUserName(user.getUsername());
            }
        }
        return PageInfo.of(all);
    }

    public List<Comment> findByCondition(Comment search, Account currentUser) {
        if (1 == currentUser.getRole()) {
            return commentDao.findBySearch(search, null, null);
        } else {
            return commentDao.findBySearch(search, currentUser.getId(), currentUser.getRole());
        }
    }
}