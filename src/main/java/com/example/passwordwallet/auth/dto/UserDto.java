package com.example.passwordwallet.auth.dto;

import com.example.passwordwallet.domain.enums.PasswordType;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    @NotBlank
    private String login;

    @NotBlank
    private String password;

    @NotNull
    private PasswordType passwordType;

    private UserLoginInfoDto userLoginInfoDto;
}
