package com.example.passwordwallet.auth.services;

import com.example.passwordwallet.auth.dto.*;
import com.example.passwordwallet.domain.User;

public interface AuthService {
    TokenResponse getToken(UserCredentials userCredentials);

    User getAuthenticatedUser(UserCredentials userCredentials);

    TokenResponse setPasswordKey(PasswordKeyDto passwordKeyDto);

    TokenResponse changePassword(PasswordChangeDto passwordChangeDto);

    TokenResponse createNewAccount(UserDto userDto);

    UserDto getUserData();
}
