package com.example.passwordwallet.auth.services.impl;

import com.example.passwordwallet.auth.dto.*;
import com.example.passwordwallet.auth.exceptions.BadCredentialException;
import com.example.passwordwallet.auth.mappers.UserMapper;
import com.example.passwordwallet.auth.repositories.UserRepository;
import com.example.passwordwallet.auth.services.AuthService;
import com.example.passwordwallet.domain.PasswordType;
import com.example.passwordwallet.domain.User;
import com.example.passwordwallet.security.JwtTokenUtil;
import com.example.passwordwallet.security.LoggedUserHelper;
import com.example.passwordwallet.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import static com.example.passwordwallet.util.HashUtil.*;
import static com.example.passwordwallet.util.HashUtil.calculateHMAC;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserMapper userMapper;

    @Override
    public TokenResponse getToken(UserCredentials userCredentials) {
        User authenticatedUser = getAuthenticatedUser(userCredentials);

        return new TokenResponse(jwtTokenUtil.generateAccessToken(authenticatedUser));
    }

    @Override
    public User getAuthenticatedUser(UserCredentials userCredentials) {
        User user = getUserByLogin(userCredentials.getLogin());

        return userRepository.findByLoginAndPassword(userCredentials.getLogin(), hashUserPassword(user, userCredentials.getPassword()))
                .orElseThrow(() -> new BadCredentialException("Bad credentials"));
    }

    @Override
    public TokenResponse setPasswordKey(PasswordKeyDto passwordKeyDto) {
        User currentUser = LoggedUserHelper.getCurrentUser();

        return new TokenResponse(jwtTokenUtil.generateAccessTokenWithKey(currentUser, passwordKeyDto.getPasswordKey()));
    }

    @Override
    public TokenResponse changePassword(PasswordChangeDto passwordChangeDto) {
        User currentUser = getAuthenticatedUser(
                new UserCredentials(LoggedUserHelper.getCurrentUser().getLogin(), passwordChangeDto.getOldPassword()));
        String hashedPassword = hashUserPassword(currentUser, passwordChangeDto.getNewPassword());

        User updatedUser = updateUserPassword(currentUser, hashedPassword);

        return new TokenResponse(jwtTokenUtil.generateAccessToken(updatedUser));
    }

    @Override
    public TokenResponse createNewAccount(UserDto userDto) {
        User newUser = userMapper.mapUserDtoToUser(userDto);
        String password = userDto.getPassword();
        newUser.setSalt(generateRandomSalt());
        newUser.setPassword(PasswordType.SHA512.equals(userDto.getPasswordType()) ?
                calculateSHA512(password, generateRandomSalt()) :
                calculateHMAC(password, EncryptionUtil.encryptPassword(password, password)));

        User createdUser = userRepository.save(newUser);

        return new TokenResponse(jwtTokenUtil.generateAccessToken(createdUser));
    }

    @Override
    public UserDto getUserData() {
        User currentUser = LoggedUserHelper.getCurrentUser();
        return new UserDto(currentUser.getLogin(), currentUser.getPassword(), currentUser.getPasswordType());
    }

    private User getUserByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new BadCredentialException("Bad credentials"));
    }

    private User updateUserPassword(User user, String hashedPassword) {
        user.setPassword(hashedPassword);
        user.setSalt(generateRandomSalt());

        return userRepository.save(user);
    }
}