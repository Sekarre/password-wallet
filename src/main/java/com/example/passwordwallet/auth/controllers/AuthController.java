package com.example.passwordwallet.auth.controllers;

import com.example.passwordwallet.auth.dto.*;
import com.example.passwordwallet.auth.services.AuthService;
import com.example.passwordwallet.domain.helper.UserLoginEventByIp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<TokenResponse> generateToken(@Valid @RequestBody UserCredentials userCredentials) {
        return ResponseEntity.ok(authService.getToken(userCredentials));
    }

    @PostMapping("/key")
    public ResponseEntity<TokenResponse> setPasswordKey(@Valid @RequestBody UserPasswordKeyDto userPasswordKeyDto) {
        return ResponseEntity.ok(authService.setPasswordKey(userPasswordKeyDto));
    }

    @PostMapping("/password-change")
    public ResponseEntity<TokenResponse> changePassword(@RequestBody @Valid UserPasswordChangeDto userPasswordChangeDto) {
        return ResponseEntity.ok(authService.changePassword(userPasswordChangeDto));
    }

    @PostMapping("/new-account")
    public ResponseEntity<TokenResponse> createNewAccount(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(authService.createNewAccount(userDto));
    }

    @GetMapping("/user-data")
    public ResponseEntity<UserDto> getUserData() {
        return ResponseEntity.ok(authService.getUserData());
    }

    @GetMapping("/user-login-data")
    public ResponseEntity<UserLoginInfoDto> getLoginInfoData() {
        return ResponseEntity.ok(authService.getLoginInfoData());
    }

    @GetMapping("/user-login-ip-data")
    public ResponseEntity<List<UserLoginEventByIpDto>> getUserLoginEventsByIp() {
        return ResponseEntity.ok(authService.getUserLoginEventsByIp());
    }

    @PatchMapping("/user-ip-ban")
    public ResponseEntity<?> banUserIp(@RequestParam String userIp) {
        authService.banUserIp(userIp);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/user-ip-unban")
    public ResponseEntity<?> unbanUserIp(@RequestParam String userIp) {
        authService.unbanUserIp(userIp);

        return ResponseEntity.noContent().build();
    }
}
