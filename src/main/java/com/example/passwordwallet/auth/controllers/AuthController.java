package com.example.passwordwallet.auth.controllers;

import com.example.passwordwallet.auth.dto.*;
import com.example.passwordwallet.auth.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<TokenResponse> generateToken(@RequestBody @Valid UserCredentials userCredentials) {
        return ResponseEntity.ok(authService.getToken(userCredentials));
    }

    @PostMapping("/key")
    public ResponseEntity<?> setPasswordKey(@RequestBody @Valid PasswordKeyDto passwordKeyDto) {
        return ResponseEntity.ok(authService.setPasswordKey(passwordKeyDto));
    }

    @PostMapping("/password-change")
    public ResponseEntity<?> changePassword(@RequestBody @Valid PasswordChangeDto passwordChangeDto) {
        return ResponseEntity.ok(authService.changePassword(passwordChangeDto));
    }

    @PostMapping("/new-account")
    public ResponseEntity<TokenResponse> createNewAccount(@RequestBody @Valid UserDto userDto) {
        return ResponseEntity.ok(authService.createNewAccount(userDto));
    }

    @GetMapping("/user-data")
    public ResponseEntity<UserDto> getUserData() {
        return ResponseEntity.ok(authService.getUserData());
    }
}
