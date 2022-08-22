package com.dickli.provider.service.user;

import com.dickli.exception.ServiceException;
import com.dickli.model.User;
import com.dickli.provider.api.user.UserRpcService;
import com.dickli.provider.api.user.dto.UserDto;
import com.dickli.service.IUserService;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@DubboService
public class UserRpcServiceImpl implements UserRpcService {
    @Autowired
    private IUserService userService;
    @Override
    public void insertUser(UserDto userDto) {
        if (Objects.isNull(userDto)) {
            throw new ServiceException("新增用户失败", "用户为空");
        }
        User user = new User();
        BeanUtils.copyProperties(userDto,user);
        userService.save(user);
    }

    @Override
    public UserDto selectByUserId(String userId) {
        if (StringUtils.isBlank(userId)) {
            throw new ServiceException("查询用户失败", "用户id为空");
        }
        User user = userService.queryList(User.builder().userId(userId).build()).stream().findFirst().get();
        if(Objects.isNull(user)){
            return null;
        }
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user,userDto);
        return userDto;
    }
}
