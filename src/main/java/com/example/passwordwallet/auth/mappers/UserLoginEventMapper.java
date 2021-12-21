package com.example.passwordwallet.auth.mappers;

import com.example.passwordwallet.auth.dto.UserLoginEventDto;
import com.example.passwordwallet.domain.UserLoginEvent;
import org.mapstruct.Mapper;

@Mapper
public abstract class UserLoginEventMapper {
    public abstract UserLoginEventDto mapUserLoginEventToUserLoginEventDto(UserLoginEvent userLoginEvent);
}
