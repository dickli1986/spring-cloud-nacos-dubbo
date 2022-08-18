package com.dickli.service.impl;

import com.alibaba.fastjson.JSON;
import com.dickli.model.User;
import com.dickli.service.IUserService;
import org.apache.dubbo.common.utils.JsonUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class UserServiceImplTest {
    @Autowired
    private IUserService userService;
    @Test
    void save() {
        User user = new User();
        user.setUserId("3");
        user.setUserName("王五");
        user.setStatus(1);
        user.setCreateTime(new Date());
        userService.save(user);
    }

    @Test
    void selectByUserId() {
        System.out.println(JSON.toJSONString(userService.selectByUserId("2")));
    }
}