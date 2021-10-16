package com.example.passwordwallet.auth.dto;

import com.example.passwordwallet.domain.PasswordType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @NotBlank
    private String login;

    @NotBlank
    private String password;

    @NotBlank
    private PasswordType passwordType;
}
