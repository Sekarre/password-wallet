package com.example.passwordwallet.passwords.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PasswordCreateDto {

    @NotBlank
    private String title;

    @NotBlank
    private String login;

    @NotBlank
    private String password;

    private String webAddress;
    private String description;
}
