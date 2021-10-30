package com.example.passwordwallet.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionUtilTest {

    @Test
    void should_encrypt_password() {
        //given
        String password = "password";
        String key = "key";

        //when
        String encrypted = EncryptionUtil.encryptPassword(password, key);

        //then
        assertNotEquals(password, encrypted);
    }

    @Test
    void should_encrypt_and_decrypt_password() {
        //given
        String password = "password";
        String key = "key";

        //when
        String encrypted = EncryptionUtil.encryptPassword(password, key);
        String decrypted = EncryptionUtil.decryptPassword(encrypted, key);

        //then
        assertEquals(password, decrypted);
    }
}