package com.dickli.provider.api.user;

import com.dickli.provider.api.user.dto.UserDto;

public interface UserRpcService {
    void insertUser(UserDto user);

    UserDto selectByUserId(String userId);
}
