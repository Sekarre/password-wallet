package com.example.passwordwallet.auth.mappers;

import com.example.passwordwallet.auth.dto.UserLoginInfoDto;
import com.example.passwordwallet.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public abstract class UserLoginInfoMapper {

    @Mapping(target = "numberOfFailureAttempts", source = "user.userLoginInfo.failureCount")
    @Mapping(target = "lastLoginDate", source = "user.userLoginInfo.lastLoginDate")
    public abstract UserLoginInfoDto mapToSummarizedUserLoginInfoDto(User user);
}
