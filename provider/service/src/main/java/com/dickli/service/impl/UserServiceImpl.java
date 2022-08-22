package com.dickli.service.impl;

import com.dickli.mapper.UserMapper;
import com.dickli.model.User;
import com.dickli.service.IUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public int save(User user) {
        return userMapper.insertSelective(user);
    }

    @Override
    public int modify(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public int removeById(Long id) {
        return userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public User queryById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<User> queryList(User user) {
        return userMapper.selectListSelective(user);
    }

    @Override
    public PageInfo<User> queryPageList(User user, int pageNum, int pageSize) {
        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> userMapper.selectListSelective(user));
    }
}