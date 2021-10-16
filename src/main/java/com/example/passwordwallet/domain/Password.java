package com.example.passwordwallet.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Password {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @NotBlank
    private String password;

    private String webAddress;

    private String description;

    private String login;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;
}
