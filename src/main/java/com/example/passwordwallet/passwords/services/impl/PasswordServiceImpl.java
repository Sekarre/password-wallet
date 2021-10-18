package com.example.passwordwallet.passwords.services.impl;

import com.example.passwordwallet.domain.Password;
import com.example.passwordwallet.domain.PasswordType;
import com.example.passwordwallet.domain.User;
import com.example.passwordwallet.exceptions.NotFoundException;
import com.example.passwordwallet.passwords.dto.PasswordDto;
import com.example.passwordwallet.passwords.dto.PasswordTypeDto;
import com.example.passwordwallet.passwords.mappers.PasswordMapper;
import com.example.passwordwallet.passwords.repositories.PasswordRepository;
import com.example.passwordwallet.passwords.services.PasswordService;
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
    private final PasswordMapper passwordMapper;

    @Override
    public Password createPassword(PasswordDto passwordDto) {

        if (Objects.isNull(getCurrentUser().getKey())) {
            throw new IllegalStateException("Key must be set to create password");
        }

        Password password = passwordMapper.mapPasswordDtoToPassword(passwordDto);
        password.setUser(getCurrentUser());
        return passwordRepository.save(password);
    }

    @Override
    public Password updatePassword(Long passwordId, PasswordDto passwordDto) {
        Password password = getPasswordByIdAndUserId(passwordId, getCurrentUser().getId());
        return passwordRepository.save(passwordMapper.mapPasswordDtoToPasswordUpdate(passwordDto, password));
    }

    @Override
    public PasswordDto getPassword(Long passwordId) {
        return passwordMapper.mapPasswordToPasswordDtoWithPassword(getPasswordByIdAndUserId(passwordId, getCurrentUser().getId()));
    }

    @Override
    public List<PasswordDto> getAllPasswords(Pageable pageable) {
        return passwordRepository.findAllByUserId(getCurrentUser().getId(), pageable).stream()
                .map(passwordMapper::mapPasswordToPasswordDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deletePassword(Long passwordId) {
        getPasswordByIdAndUserId(passwordId, getCurrentUser().getId());
        passwordRepository.deleteById(passwordId);
    }

    @Override
    public void updatePasswordsWithNewUser(User user) {
        List<Password> passwordList = passwordRepository.findAllByUserId(user.getId());
        passwordList.forEach(p -> p.setPassword(EncryptionUtil.encryptPassword(p.getPassword(), user.getKey())));

        passwordRepository.saveAll(passwordList);
    }

    @Override
    public List<PasswordTypeDto> getPasswordTypes() {
        return Arrays.stream(PasswordType.values())
                .map(passwordType -> new PasswordTypeDto(passwordType.name()))
                .collect(Collectors.toList());
    }

    private Password getPasswordByIdAndUserId(Long id, Long userId) {
        return passwordRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException("Password with given id not found: " + id));
    }
}
