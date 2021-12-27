package com.example.passwordwallet.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class SharedPassword {

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

    private Long originalPasswordId;

    private Long ownerUserId;

    private String ownerUserEmail;

    private LocalDateTime creationDate;

    @JoinColumn(name = "user_id")
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private User sharedToUser;
}
