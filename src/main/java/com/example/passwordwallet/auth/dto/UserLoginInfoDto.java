package com.example.passwordwallet.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.passwordwallet.util.DateUtil.JSON_DATE_TIME_PATTERN;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginInfoDto {

    @JsonFormat(pattern = JSON_DATE_TIME_PATTERN)
    private LocalDateTime lastLoginDate;

    private Integer numberOfFailureAttempts;
    private Integer numberOfOldFailureAttempts;

    private List<UserLoginEventDto> userLoginEvents;
}
