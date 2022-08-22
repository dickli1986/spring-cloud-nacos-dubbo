package com.dickli.service.impl;

import com.alibaba.fastjson.JSON;
import com.dickli.model.User;
import com.dickli.service.IUserService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
class UserServiceImplTest {
    @Autowired
    private IUserService userService;

    @Test
    void queryList() {
        List<User> userList = userService.queryList(null);
        log.info("===========" + JSON.toJSONString(userList));
    }
}