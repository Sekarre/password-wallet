package com.example.passwordwallet.domain;

import com.example.passwordwallet.domain.enums.PasswordType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String login;

    @NotBlank
    private String password;

    @NotBlank
    private String email;

    private String salt;

    @Enumerated(EnumType.STRING)
    private PasswordType passwordType;

    @OneToMany(mappedBy = "user")
    private List<Password> passwords;

    @OneToMany(mappedBy = "sharedToUser")
    private List<SharedPassword> passwordsSharedToUser;

    @OneToOne(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private UserLoginInfo userLoginInfo;

}
