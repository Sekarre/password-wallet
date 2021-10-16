package com.example.passwordwallet.util;

import com.example.passwordwallet.domain.PasswordType;
import com.example.passwordwallet.domain.User;
import org.apache.commons.codec.digest.HmacUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.apache.commons.codec.digest.HmacAlgorithms.HMAC_SHA_512;

public class HashUtil {

    public static String hashUserPassword(User user, String password) {
        return PasswordType.SHA512.equals(user.getPasswordType()) ?
                calculateSHA512(password, user.getSalt()) :
                calculateHMAC(password, EncryptionUtil.encryptPassword(password, password));
    }

    public static String calculateSHA512(String password, String salt) {
        String text = password + salt;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(text.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String calculateHMAC(String text, String key) {
        return new HmacUtils(HMAC_SHA_512, key).hmacHex(text);
    }

    public static byte[] calculateMD5(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(text.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
