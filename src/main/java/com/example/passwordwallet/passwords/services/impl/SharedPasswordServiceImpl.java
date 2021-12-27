package com.example.passwordwallet.passwords.services.impl;

import com.example.passwordwallet.domain.Password;
import com.example.passwordwallet.domain.SharedPassword;
import com.example.passwordwallet.domain.User;
import com.example.passwordwallet.exceptions.NotFoundException;
import com.example.passwordwallet.passwords.dto.SharedPasswordDto;
import com.example.passwordwallet.passwords.mappers.SharedPasswordMapper;
import com.example.passwordwallet.passwords.repositories.SharedPasswordRepository;
import com.example.passwordwallet.passwords.services.SharedPasswordService;
import com.example.passwordwallet.security.LoggedUserHelper;
import com.example.passwordwallet.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.passwordwallet.security.LoggedUserHelper.getCurrentUser;

@Slf4j
@Service
@RequiredArgsConstructor
public class SharedPasswordServiceImpl implements SharedPasswordService {

    private final SharedPasswordRepository sharedPasswordRepository;
    private final SharedPasswordMapper sharedPasswordMapper;

    @Override
    public void sharePassword(User userByEmail, Password password) {
        if (sharedPasswordRepository.existsByOriginalPasswordIdAndSharedToUserId(password.getId(), userByEmail.getId())) {
            return;
        }

        if (userByEmail.getId().equals(getCurrentUser().getId())) {
            throw new IllegalStateException("Cannot share password to yourself");
        }

        sharedPasswordRepository.save(getNewSharedPassword(userByEmail, password));
    }

    @Override
    public List<SharedPasswordDto> getAllSharedFromPasswords(Pageable pageable) {
        return sharedPasswordRepository.findAllBySharedToUserId(getCurrentUser().getId(), pageable).stream()
                .map(sharedPasswordMapper::mapSharedPasswordToSharedPasswordDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SharedPasswordDto> getAllSharedToPasswords(Pageable pageable) {
        return sharedPasswordRepository.findAllByOwnerUserId(getCurrentUser().getId(), pageable).stream()
                .map(sharedPasswordMapper::mapSharedPasswordToSharedPasswordDto)
                .collect(Collectors.toList());
    }

    @Override
    public SharedPasswordDto getSharedPasswordById(Long sharedPasswordId) {
        return sharedPasswordRepository.findById(sharedPasswordId)
                .map(sharedPasswordMapper::mapSharedPasswordToSharedPasswordDtoWithPassword)
                .orElseThrow(() -> new NotFoundException("Password not found"));
    }

    @Override
    public void updateSharedPasswordIfNeeded(Password password) {
        sharedPasswordRepository.findAllByOriginalPasswordId(password.getId())
                .forEach(sharedPassword -> sharedPasswordRepository.save(getUpdatedSharedPassword(sharedPassword, password)));
    }

    @Override
    public void deleteSharedPassword(Password password) {
        sharedPasswordRepository.deleteAllByOriginalPasswordId(password.getId());
    }

    @Override
    public void removePasswordSharing(Long sharedPasswordId) {
        sharedPasswordRepository.deleteById(sharedPasswordId);
    }

    private SharedPassword getUpdatedSharedPassword(SharedPassword sharedPassword, Password password) {
        String decryptedPassword = EncryptionUtil.decryptPassword(password.getPassword(), LoggedUserHelper.getCurrentUser().getPassword());
        String newEncryptedPassword = EncryptionUtil.encryptPassword(decryptedPassword, sharedPassword.getSharedToUser().getPassword());

        return SharedPassword.builder()
                .id(sharedPassword.getId())
                .description(password.getDescription())
                .login(password.getLogin())
                .password(newEncryptedPassword)
                .title(password.getTitle())
                .webAddress(password.getWebAddress())
                .originalPasswordId(sharedPassword.getOriginalPasswordId())
                .ownerUserId(sharedPassword.getOwnerUserId())
                .ownerUserEmail(sharedPassword.getOwnerUserEmail())
                .sharedToUser(sharedPassword.getSharedToUser())
                .build();
    }

    private SharedPassword getNewSharedPassword(User userToShare, Password password) {
        String decryptedPassword = EncryptionUtil.decryptPassword(password.getPassword(), LoggedUserHelper.getCurrentUser().getPassword());
        String newEncryptedPassword = EncryptionUtil.encryptPassword(decryptedPassword, userToShare.getPassword());

        return SharedPassword.builder()
                .description(password.getDescription())
                .login(password.getLogin())
                .password(newEncryptedPassword)
                .title(password.getTitle())
                .webAddress(password.getWebAddress())
                .originalPasswordId(password.getId())
                .ownerUserId(getCurrentUser().getId())
                .ownerUserEmail(getCurrentUser().getEmail())
                .sharedToUser(userToShare)
                .creationDate(LocalDateTime.now())
                .build();
    }
}
