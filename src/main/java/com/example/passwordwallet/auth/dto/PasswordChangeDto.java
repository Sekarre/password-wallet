package com.example.passwordwallet.auth.dto;

import com.example.passwordwallet.domain.PasswordType;
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
public class PasswordChangeDto {

    @NotBlank
    private String currentPassword;

    @NotBlank
    private String newPassword;

    @NotNull
    private PasswordType passwordType;
}
