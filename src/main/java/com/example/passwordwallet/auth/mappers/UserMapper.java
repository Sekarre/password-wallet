package com.example.passwordwallet.auth.mappers;

import com.example.passwordwallet.auth.dto.UserDto;
import com.example.passwordwallet.domain.User;
import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        builder = @Builder(disableBuilder = true),
        uses = UserLoginInfoMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public abstract class UserMapper {

    public abstract User mapUserDtoToUser(UserDto userDto);

    @Mapping(target = "userLoginInfoDto", source = "user")
    public abstract UserDto mapUserToUserDto(User user);
}
