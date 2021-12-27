package com.example.passwordwallet.passwords.services;

import com.example.passwordwallet.domain.Password;
import com.example.passwordwallet.domain.User;
import com.example.passwordwallet.passwords.dto.SharedPasswordDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SharedPasswordService {

    void sharePassword(User userToShare, Password password);

    List<SharedPasswordDto> getAllSharedFromPasswords(Pageable pageable);

    List<SharedPasswordDto> getAllSharedToPasswords(Pageable pageable);

    SharedPasswordDto getSharedPasswordById(Long sharedPasswordId);

    void updateSharedPasswordIfNeeded(Password password);

    void deleteSharedPassword(Password password);

    void removePasswordSharing(Long sharedPasswordId);
}
