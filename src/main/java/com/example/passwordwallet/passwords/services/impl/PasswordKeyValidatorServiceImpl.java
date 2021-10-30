package com.example.passwordwallet.passwords.services.impl;

import com.example.passwordwallet.domain.Password;
import com.example.passwordwallet.exceptions.BadKeyException;
import com.example.passwordwallet.passwords.services.PasswordKeyValidatorService;
import com.example.passwordwallet.util.EncryptionUtil;
import org.springframework.stereotype.Service;

@Service
public class PasswordKeyValidatorServiceImpl implements PasswordKeyValidatorService {

    @Override
    public boolean isKeyValid(Password password, String key) {
        try {
            return !EncryptionUtil.decryptPassword(password.getPassword(), key).isBlank();
        } catch (BadKeyException e) {
            return false;
        }
    }
}
