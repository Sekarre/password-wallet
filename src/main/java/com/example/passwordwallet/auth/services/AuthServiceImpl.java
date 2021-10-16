package com.example.passwordwallet.auth.services;

import com.example.passwordwallet.auth.dto.PasswordChangeDto;
import com.example.passwordwallet.auth.dto.PasswordKeyDto;
import com.example.passwordwallet.auth.dto.TokenResponse;
import com.example.passwordwallet.auth.dto.UserCredentials;
import com.example.passwordwallet.auth.exceptions.BadCredentialException;
import com.example.passwordwallet.auth.repositories.UserRepository;
import com.example.passwordwallet.domain.User;
import com.example.passwordwallet.security.JwtTokenUtil;
import com.example.passwordwallet.security.LoggedUserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.example.passwordwallet.util.HashUtil.hashUserPassword;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;

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

    private User getUserByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new BadCredentialException("Bad credentials"));
    }

    private User updateUserPassword(User user, String hashedPassword) {
        user.setPassword(hashedPassword);
        user.setSalt(UUID.randomUUID().toString());

        return userRepository.save(user);
    }
}
