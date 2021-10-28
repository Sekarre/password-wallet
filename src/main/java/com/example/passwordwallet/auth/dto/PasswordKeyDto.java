package com.example.passwordwallet.auth.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordKeyDto {

    @NotBlank
    private String passwordKey;
}
