package com.example.passwordwallet.factories;

import com.example.passwordwallet.auth.dto.UserCredentials;
import com.example.passwordwallet.auth.dto.UserDto;
import com.example.passwordwallet.auth.dto.UserPasswordChangeDto;
import com.example.passwordwallet.auth.dto.UserPasswordKeyDto;
import com.example.passwordwallet.domain.PasswordType;
import com.example.passwordwallet.domain.User;

public class UserMockFactory {

    public static User buildDefaultUserMock() {
        return User.builder()
                .login("login")
                .password("password")
                .passwordType(PasswordType.HMAC)
                .salt("salt")
                .key("key")
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

    public static UserPasswordChangeDto buildUserPasswordChangeDtoMock() {
        return UserPasswordChangeDto.builder()
                .currentPassword("password")
                .newPassword("newPassword")
                .passwordType(PasswordType.HMAC)
                .build();
    }

    public static UserPasswordKeyDto buildUserPasswordKeyDtoMock() {
        return UserPasswordKeyDto.builder()
                .passwordKey("passwordKey")
                .build();
    }
}
