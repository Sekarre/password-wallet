package com.example.passwordwallet.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LockTime {

    NONE(0, 0),
    SHORT(1, 1),
    MEDIUM(2, 5),
    LONG(3, 10),
    SAYONARA(4, 120);

    private final Integer failureCount;
    private final Integer timeInSeconds;
}
