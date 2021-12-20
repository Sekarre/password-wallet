package com.example.passwordwallet.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class UserLoginEvent {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private LocalDateTime loginDate;

    private boolean loginSuccessful;

    private String ipAddress;

    private Long userId;

    @JoinColumn(name = "summarized_login_info_id")
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private UserLoginInfo userLoginInfo;
}
