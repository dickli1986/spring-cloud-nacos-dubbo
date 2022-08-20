package com.dickli.provider.service.user;

import com.alibaba.fastjson.JSON;
import com.dickli.provider.api.user.UserRpcService;
import com.dickli.provider.api.user.dto.UserDto;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class UserRpcServiceImplTest {
    @DubboReference
    private UserRpcService userRpcService;
    @Test
    void insertUser() {
        UserDto userDto = UserDto.builder().userId("2").userName("你好").build();
        userRpcService.insertUser(userDto);
    }

    @Test
    void selectByUserId() {
        UserDto userDto = userRpcService.selectByUserId("2");
        System.out.println("========="+ JSON.toJSONString(userDto));
    }
}