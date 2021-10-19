package com.example.passwordwallet.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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
    private String salt;

    @Enumerated(EnumType.STRING)
    private PasswordType passwordType;

    @OneToMany(mappedBy = "user")
    private List<Password> passwords;

    @Transient
    private String key;
}
