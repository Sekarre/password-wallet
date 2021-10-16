package com.example.passwordwallet.passwords.services;

import com.example.passwordwallet.domain.User;
import com.example.passwordwallet.passwords.dto.PasswordDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PasswordService {
    void createPassword(PasswordDto passwordDto);

    void updatePassword(Long passwordId, PasswordDto passwordDto);

    PasswordDto getPassword(Long passwordId);

    Page<PasswordDto> getAllPasswords(Pageable pageable);

    void deletePassword(Long passwordId);

    void updatePasswordsWithNewUser(User user);
}
