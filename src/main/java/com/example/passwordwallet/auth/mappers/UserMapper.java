package com.example.passwordwallet.auth.mappers;

import com.example.passwordwallet.auth.dto.UserDto;
import com.example.passwordwallet.domain.User;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(builder = @Builder(disableBuilder = true))
public abstract class UserMapper {

    public abstract User mapUserDtoToUser(UserDto userDto);
}
