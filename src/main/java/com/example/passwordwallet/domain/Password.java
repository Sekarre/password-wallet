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
    private String title;

    @NotBlank
    private String login;

    @NotBlank
    private String password;

    private String webAddress;

    private String description;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;
}
