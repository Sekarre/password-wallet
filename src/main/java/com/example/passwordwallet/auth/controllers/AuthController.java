package com.example.passwordwallet.auth.controllers;

import com.example.passwordwallet.auth.dto.PasswordChangeDto;
import com.example.passwordwallet.auth.dto.PasswordKeyDto;
import com.example.passwordwallet.auth.dto.TokenResponse;
import com.example.passwordwallet.auth.dto.UserCredentials;
import com.example.passwordwallet.auth.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody @Validated PasswordChangeDto passwordChangeDto) {
        return ResponseEntity.ok(authService.changePassword(passwordChangeDto));
    }
}
