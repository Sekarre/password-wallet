package com.example.passwordwallet.passwords.services;

import com.example.passwordwallet.domain.Password;

public interface PasswordKeyValidatorService {

    boolean isKeyValid(Password password, String key);
}
