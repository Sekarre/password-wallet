package com.example.passwordwallet.auth.services;

import com.example.passwordwallet.auth.dto.PasswordChangeDto;
import com.example.passwordwallet.domain.User;
import com.example.passwordwallet.auth.dto.PasswordKeyDto;
import com.example.passwordwallet.auth.dto.TokenResponse;
import com.example.passwordwallet.auth.dto.UserCredentials;

public interface AuthService {
    TokenResponse getToken(UserCredentials userCredentials);

    User getAuthenticatedUser(UserCredentials userCredentials);

    TokenResponse setPasswordKey(PasswordKeyDto passwordKeyDto);

    TokenResponse changePassword(PasswordChangeDto passwordChangeDto);
}
