package com.example.passwordwallet.auth.dto;

import com.example.passwordwallet.domain.PasswordType;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPasswordChangeDto {

    @NotBlank
    private String currentPassword;

    @NotBlank
    private String newPassword;

    @NotNull
    private PasswordType passwordType;
}
