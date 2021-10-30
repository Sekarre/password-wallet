package com.example.passwordwallet.passwords.services;

import com.example.passwordwallet.domain.Password;
import com.example.passwordwallet.domain.User;
import com.example.passwordwallet.passwords.dto.PasswordCreateDto;
import com.example.passwordwallet.passwords.dto.PasswordDto;
import com.example.passwordwallet.passwords.dto.PasswordTypeDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PasswordService {

    Password createPassword(PasswordCreateDto passwordDto);

    Password updatePassword(Long passwordId, PasswordDto passwordDto);

    PasswordDto getPassword(Long passwordId);

    List<PasswordDto> getAllPasswords(Pageable pageable);

    boolean checkIfPasswordKeyValidForAll(String key);

    void deletePassword(Long passwordId);

    void updatePasswordsWithNewUser(User user);

    List<PasswordTypeDto> getPasswordTypes();
}
