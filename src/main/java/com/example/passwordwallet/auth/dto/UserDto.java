package com.example.passwordwallet.auth.dto;

import com.example.passwordwallet.domain.PasswordType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @NotBlank
    private String login;

    @NotBlank
    private String password;

    @NotNull
    private PasswordType passwordType;
}
