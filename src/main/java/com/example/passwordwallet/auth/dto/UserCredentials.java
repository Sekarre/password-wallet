package com.example.passwordwallet.auth.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCredentials {

    @NotBlank
    private String login;

    @NotBlank
    private String password;
}
