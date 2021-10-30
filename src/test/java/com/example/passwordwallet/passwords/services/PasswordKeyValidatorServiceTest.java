package com.example.passwordwallet.passwords.services;

import com.example.passwordwallet.domain.Password;
import com.example.passwordwallet.factories.PasswordMockFactory;
import com.example.passwordwallet.passwords.services.impl.PasswordKeyValidatorServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class PasswordKeyValidatorServiceTest {

    private final PasswordKeyValidatorService passwordKeyValidatorService = new PasswordKeyValidatorServiceImpl();

    @Test
    void should_check_if_key_valid_and_return_false() {
        //given
        String key = "key";
        Password password = PasswordMockFactory.buildPasswordMock();

        //when
        boolean result = passwordKeyValidatorService.isKeyValid(password, key);

        //then
        assertFalse(result);
    }
}