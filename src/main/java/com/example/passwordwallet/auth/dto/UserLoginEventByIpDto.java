package com.example.passwordwallet.auth.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserLoginEventByIpDto {

    private Long userId;
    private String ip;
    private Long loginCount;
    private boolean isBanned;
}
