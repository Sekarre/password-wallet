package com.example.passwordwallet.util;

import com.example.passwordwallet.auth.exceptions.BadCredentialException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

public class EncryptionUtil {

    private static final String ALGO = "AES";

    public static String encryptPassword(String password, String key) {
        try {
            return encrypt(password, generateKey(key));
        } catch (Exception e) {
            throw new BadCredentialException("encrypt error");
        }
    }

    public static String decryptPassword(String password, String key) {
        try {
            return decrypt(password, generateKey(key));
        } catch (Exception e) {
            throw new BadCredentialException("decrypt error");
        }
    }

    private static String encrypt(String data, Key key) throws Exception {
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encVal);
    }

    private static String decrypt(String encryptedData, Key key) throws Exception {
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = Base64.getDecoder().decode(encryptedData);
        byte[] decValue = c.doFinal(decodedValue);
        return new String(decValue);
    }

    private static Key generateKey(String password) {
        return new SecretKeySpec(HashUtil.calculateMD5(password), ALGO);
    }
}
