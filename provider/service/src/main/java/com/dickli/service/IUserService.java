package com.dickli.service;

import com.dickli.model.User;
import com.github.pagehelper.PageInfo;
import java.util.List;

public interface IUserService {
    int save(User user);

    int modify(User user);

    int removeById(Long id);

    User queryById(Long id);

    List<User> queryList(User user);

    PageInfo<User> queryPageList(User user, int pageNum, int pageSize);
}