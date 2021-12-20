package com.example.passwordwallet.domain.helper;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserLoginEventByIp {

    private Long userId;
    private String ip;
    private Long loginCount;
}
