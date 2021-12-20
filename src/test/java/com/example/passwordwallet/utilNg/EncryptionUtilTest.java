package com.example.passwordwallet.utilNg;

import com.example.passwordwallet.util.EncryptionUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EncryptionUtilTest {

    @DataProvider(name = "encryptPasswordDataProvider")
    public static Object[][] encryptPasswordDataProvider() {
        return new Object[][]{
                {"password", "key"}, {"test1", "key1"}, {"test2", "key2"}, {"test3", "key3"}, {"test5", "key5"}, {"t", "k"}
        };
    }

    @DataProvider(name = "decryptPasswordDataProvider")
    public static Object[][] decryptPasswordDataProvider() {
        return new Object[][]{
                {"passwordD", "keyD"}, {"test1D", "key1D"}, {"test2D", "key2D"}, {"test3D", "key3D"}, {"test5D", "key5D"}, {"tD", "kD"}
        };
    }

    @Test(dataProvider = "encryptPasswordDataProvider")
    void should_encrypt_password(String password, String key) {
        //when
        String encrypted = EncryptionUtil.encryptPassword(password, key);

        //then
        assertNotEquals(password, encrypted);
    }

    @Test(dataProvider = "decryptPasswordDataProvider")
    void should_encrypt_and_decrypt_password(String password, String key) {
        //when
        String encrypted = EncryptionUtil.encryptPassword(password, key);
        String decrypted = EncryptionUtil.decryptPassword(encrypted, key);

        //then
        assertEquals(password, decrypted);
    }

}