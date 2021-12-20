package com.example.passwordwallet.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class UserLoginInfo {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private LocalDateTime lastLoginDate;
    private LocalDateTime lastSuccessfulLoginDate;
    private Integer failureCount;
    private LocalDateTime lockedTo;

    @JoinColumn(name = "user_id")
    @OneToOne(cascade = CascadeType.MERGE)
    private User user;

    @OneToMany(mappedBy = "userLoginInfo", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REMOVE, CascadeType.PERSIST})
    private List<UserLoginEvent> userLoginEvents = new ArrayList<>();
}
