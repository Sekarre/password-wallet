package com.example.passwordwallet.passwords.services.impl;

import com.example.passwordwallet.auth.services.UserService;
import com.example.passwordwallet.domain.Password;
import com.example.passwordwallet.domain.User;
import com.example.passwordwallet.domain.enums.PasswordType;
import com.example.passwordwallet.exceptions.BadKeyException;
import com.example.passwordwallet.exceptions.NotFoundException;
import com.example.passwordwallet.passwords.dto.PasswordCreateDto;
import com.example.passwordwallet.passwords.dto.PasswordDto;
import com.example.passwordwallet.passwords.dto.PasswordTypeDto;
import com.example.passwordwallet.passwords.dto.SharedPasswordDto;
import com.example.passwordwallet.passwords.mappers.PasswordMapper;
import com.example.passwordwallet.passwords.repositories.PasswordRepository;
import com.example.passwordwallet.passwords.services.PasswordKeyValidatorService;
import com.example.passwordwallet.passwords.services.PasswordService;
import com.example.passwordwallet.passwords.services.SharedPasswordService;
import com.example.passwordwallet.security.LoggedUserHelper;
import com.example.passwordwallet.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.passwordwallet.security.LoggedUserHelper.getCurrentUser;

@RequiredArgsConstructor
@Service
public class PasswordServiceImpl implements PasswordService {

    private final PasswordRepository passwordRepository;
    private final SharedPasswordService sharedPasswordService;
    private final PasswordMapper passwordMapper;
    private final PasswordKeyValidatorService passwordKeyValidatorService;
    private final UserService userService;

    @Override
    public Password createPassword(PasswordCreateDto passwordDto) {

        if (Objects.isNull(getCurrentUser().getPassword())) {
            throw new IllegalStateException("Key must be set to create password");
        }

        Password password = passwordMapper.mapPasswordDtoToPassword(passwordDto);
        password.setUser(getCurrentUser());

        return passwordRepository.save(password);
    }

    @Override
    public Password updatePassword(Long passwordId, PasswordDto passwordDto) {
        Password password = getPasswordByIdAndUserId(passwordId, getCurrentUser().getId());

        if (passwordKeyValidatorService.isKeyValid(password, LoggedUserHelper.getCurrentUser().getPassword())) {
            Password savedPsw = passwordRepository.save(passwordMapper.mapPasswordDtoToPasswordUpdate(passwordDto, password));
            sharedPasswordService.updateSharedPasswordIfNeeded(savedPsw);
            return savedPsw;
        }

        throw new BadKeyException("Bad key");
    }

    @Override
    public PasswordDto getPassword(Long passwordId) {
        Password password = getPasswordByIdAndUserId(passwordId, getCurrentUser().getId());

        if (passwordKeyValidatorService.isKeyValid(password, LoggedUserHelper.getCurrentUser().getPassword())) {
            return passwordMapper.mapPasswordToPasswordDtoWithPassword(password);
        }

        throw new BadKeyException("Bad key");
    }

    @Override
    public List<PasswordDto> getAllPasswords(Pageable pageable) {
        return passwordRepository.findAllByUserId(getCurrentUser().getId(), pageable).stream()
                .map(passwordMapper::mapPasswordToPasswordDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkIfPasswordKeyValidForAll(String key) {
        List<Password> passwords = passwordRepository.findAllByUserId(getCurrentUser().getId());

        if (!passwords.isEmpty()) {
            if (passwords.stream().anyMatch(password -> !passwordKeyValidatorService.isKeyValid(password, key))) {
                throw new BadKeyException("Password key is not valid");
            }
        }

        return true;
    }

    @Override
    public void deletePassword(Long passwordId) {
        Password password = getPasswordByIdAndUserId(passwordId, getCurrentUser().getId());

        if (passwordKeyValidatorService.isKeyValid(password, LoggedUserHelper.getCurrentUser().getPassword())) {
            passwordRepository.delete(password);
            sharedPasswordService.deleteSharedPassword(password);

            return;
        }

        throw new BadKeyException("Bad key");
    }

    @Override
    public void sharePassword(Long passwordId, String email) {
        User userToShare = userService.getUserByEmail(email);
        Password password = getPasswordByIdAndUserId(passwordId, getCurrentUser().getId());

        sharedPasswordService.sharePassword(userToShare, password);
    }

    @Override
    public List<SharedPasswordDto> getAllSharedToPasswords(Pageable pageable) {
        return sharedPasswordService.getAllSharedToPasswords(pageable);
    }

    @Override
    public List<SharedPasswordDto> getAllSharedFromPasswords(Pageable pageable) {
        return sharedPasswordService.getAllSharedFromPasswords(pageable);
    }

    @Override
    public SharedPasswordDto getSharedPasswordById(Long sharedPasswordId) {
        return sharedPasswordService.getSharedPasswordById(sharedPasswordId);
    }

    @Override
    public void removePasswordSharing(Long sharedPasswordId) {
        sharedPasswordService.removePasswordSharing(sharedPasswordId);
    }

    @Override
    public void updatePasswordsWithNewUser(User user) {
        List<Password> passwordList = passwordRepository.findAllByUserId(user.getId());
        passwordList.forEach(p -> p.setPassword(EncryptionUtil.encryptPassword(p.getPassword(), user.getPassword())));

        passwordRepository.saveAll(passwordList);
    }

    @Override
    public List<PasswordTypeDto> getPasswordTypes() {
        return Arrays.stream(PasswordType.values())
                .map(passwordType -> new PasswordTypeDto(passwordType.name()))
                .collect(Collectors.toList());
    }

    public Password getPasswordByIdAndUserId(Long id, Long userId) {
        return passwordRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException("Password with given id not found: " + id));
    }
}
