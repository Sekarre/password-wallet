package com.example.passwordwallet.auth.controllers;

import com.example.passwordwallet.auth.dto.*;
import com.example.passwordwallet.auth.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<TokenResponse> generateToken(@RequestBody @Validated UserCredentials userCredentials) {
        return ResponseEntity.ok(authService.getToken(userCredentials));
    }

    @PostMapping("/key")
    public ResponseEntity<?> setPasswordKey(@RequestBody @Validated PasswordKeyDto passwordKeyDto) {
        return ResponseEntity.ok(authService.setPasswordKey(passwordKeyDto));
    }

    @PostMapping("/password-change")
    public ResponseEntity<?> changePassword(@RequestBody @Validated PasswordChangeDto passwordChangeDto) {
        return ResponseEntity.ok(authService.changePassword(passwordChangeDto));
    }

    @PostMapping("/new-account")
    public ResponseEntity<?> createNewAccount(@RequestBody @Validated UserDto userDto) {
        authService.createNewAccount(userDto);

        return ResponseEntity.ok().build();
    }
}
