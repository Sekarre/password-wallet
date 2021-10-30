package com.example.passwordwallet.auth.mappers;

import com.example.passwordwallet.auth.dto.UserDto;
import com.example.passwordwallet.domain.User;
import com.example.passwordwallet.factories.UserMockFactory;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void should_map_user_dto_to_user() {
        //given
        UserDto source = UserMockFactory.buildUserDtoMock();

        //when
        User result = userMapper.mapUserDtoToUser(source);

        //then
        assertEquals(source.getLogin(), result.getLogin());
        assertEquals(source.getPassword(), result.getPassword());
        assertEquals(source.getPasswordType(), result.getPasswordType());
    }

    @Test
    void should_map_user_to_user_dto() {
        //given
        User source = UserMockFactory.buildDefaultUserMock();

        //when
        UserDto result = userMapper.mapUserToUserDto(source);

        //then
        assertEquals(source.getLogin(), result.getLogin());
        assertEquals(source.getPassword(), result.getPassword());
        assertEquals(source.getPasswordType(), result.getPasswordType());
    }
}