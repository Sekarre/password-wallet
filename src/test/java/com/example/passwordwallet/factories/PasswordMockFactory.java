package com.example.passwordwallet.factories;

import com.example.passwordwallet.domain.Password;
import com.example.passwordwallet.domain.PasswordType;
import com.example.passwordwallet.passwords.dto.PasswordCreateDto;
import com.example.passwordwallet.passwords.dto.PasswordDto;
import com.example.passwordwallet.passwords.dto.PasswordTypeDto;

public class PasswordMockFactory {

    public static Password buildPasswordMock() {
        return Password.builder()
                .id(1L)
                .title("title")
                .login("login")
                .password("password")
                .webAddress("address")
                .description("description")
                .user(UserMockFactory.buildDefaultUserMock())
                .build();
    }

    public static PasswordCreateDto buildPasswordCreateDtoMock() {
        return PasswordCreateDto.builder()
                .title("title")
                .login("login")
                .password("password")
                .webAddress("address")
                .description("description")
                .build();
    }

    public static PasswordDto buildPasswordDtoMock() {
        return PasswordDto.builder()
                .id(1L)
                .title("title")
                .login("login")
                .password("password")
                .webAddress("address")
                .description("description")
                .build();
    }

    public static PasswordTypeDto buildPasswordTypeDtoMock() {
        return PasswordTypeDto.builder()
                .name(PasswordType.SHA512.name())
                .build();
    }
}
