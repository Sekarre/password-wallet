package com.example.passwordwallet.factories;

import com.example.passwordwallet.auth.dto.UserCredentials;
import com.example.passwordwallet.auth.dto.UserDto;
import com.example.passwordwallet.domain.PasswordType;
import com.example.passwordwallet.domain.User;

public class UserMockFactory {

    public static User buildDefaultUserMock() {
        return User.builder()
                .login("login")
                .password("password")
                .passwordType(PasswordType.HMAC)
                .salt("salt")
                .build();
    }

    public static UserDto buildUserDtoMock() {
        return UserDto.builder()
                .login("login")
                .password("password")
                .passwordType(PasswordType.HMAC)
                .build();
    }

    public static UserCredentials buildUserCredentialsMock() {
        return UserCredentials.builder()
                .login("login")
                .password("password")
                .build();
    }
}
