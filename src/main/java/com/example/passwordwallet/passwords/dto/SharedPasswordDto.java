package com.example.passwordwallet.passwords.dto;

import com.example.passwordwallet.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SharedPasswordDto {

    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String login;

    @NotBlank
    private String password;

    private String webAddress;
    private String description;
    private String sharedToUserEmail;
    private String ownerUserEmail;

    @JsonFormat(pattern = DateUtil.JSON_DATE_TIME_PATTERN)
    private LocalDateTime creationDate;
}
