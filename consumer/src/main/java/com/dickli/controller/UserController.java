package com.dickli.controller;

import com.dickli.controller.result.Result;
import com.dickli.provider.api.user.UserRpcService;
import com.dickli.provider.api.user.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("user")
@Slf4j
public class UserController {
    @DubboReference(version = "1.0.0")
    private UserRpcService userRpcService;

    @RequestMapping("insert")
    @ResponseBody
    public Result<Boolean> insert(String userId,String userName){
        if(StringUtils.isBlank(userId)){
            return Result.fail("用户id为空");
        }
        if(StringUtils.isBlank(userName)){
            return Result.fail("用户姓名为空");
        }
        UserDto userDto = new UserDto(userId,userName,1,null);
        try {
            userRpcService.insertUser(userDto);
            return Result.success(Boolean.TRUE);
        } catch (Exception e) {
            log.error("新增用户失败,userId={},userName={}",userId,userName,e);
            return Result.fail("新增用户失败");
        }
    }

    @RequestMapping("selectByUserId")
    @ResponseBody
    public Result<UserDto> selectByUserId(String userId){
        if(StringUtils.isBlank(userId)){
            return Result.fail("用户id为空");
        }
        UserDto userDto = userRpcService.selectByUserId(userId);
        return Result.success(userDto);
    }
}
