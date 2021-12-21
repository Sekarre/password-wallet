package com.example.passwordwallet.auth.dto;

import com.example.passwordwallet.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserLoginEventDto {

    @JsonFormat(pattern = DateUtil.JSON_DATE_TIME_PATTERN)
    private LocalDateTime loginDate;

    private boolean loginSuccessful;

    private String ipAddress;
}
