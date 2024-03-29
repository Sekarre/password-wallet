package com.example.passwordwallet.passwords.controllers;

import com.example.passwordwallet.passwords.dto.PasswordCreateDto;
import com.example.passwordwallet.passwords.dto.PasswordDto;
import com.example.passwordwallet.passwords.dto.PasswordTypeDto;
import com.example.passwordwallet.passwords.dto.SharedPasswordDto;
import com.example.passwordwallet.passwords.services.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/password-wallet")
public class PasswordWalletController {

    private final PasswordService passwordService;

    @PostMapping
    public ResponseEntity<?> addNewPassword(@RequestBody @Valid PasswordCreateDto passwordDto) {
        passwordService.createPassword(passwordDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{passwordId}")
    public ResponseEntity<?> updatePassword(@PathVariable Long passwordId, @RequestBody @Valid PasswordDto passwordDto) {
        passwordService.updatePassword(passwordId, passwordDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{passwordId}")
    public ResponseEntity<PasswordDto> getPassword(@PathVariable Long passwordId) {
        return ResponseEntity.ok(passwordService.getPassword(passwordId));
    }

    @GetMapping
    public ResponseEntity<List<PasswordDto>> getAllPasswords(Pageable pageable) {
        return ResponseEntity.ok(passwordService.getAllPasswords(pageable));
    }

    @DeleteMapping("/{passwordId}")
    public ResponseEntity<?> deletePassword(@PathVariable Long passwordId) {
        passwordService.deletePassword(passwordId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/shared-passwords/{passwordId}")
    public ResponseEntity<?> sharePassword(@PathVariable Long passwordId, @RequestParam String userEmail) {
        passwordService.sharePassword(passwordId, userEmail);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/shared-passwords/shared-to")
    public ResponseEntity<List<SharedPasswordDto>> getAllSharedToPasswords(Pageable pageable) {
        return ResponseEntity.ok(passwordService.getAllSharedToPasswords(pageable));
    }

    @GetMapping("/shared-passwords/shared-from")
    public ResponseEntity<List<SharedPasswordDto>> getAllSharedFromPasswords(Pageable pageable) {
        return ResponseEntity.ok(passwordService.getAllSharedFromPasswords(pageable));
    }

    @GetMapping("/shared-passwords/{passwordId}")
    public ResponseEntity<SharedPasswordDto> getSharedPassword(@PathVariable Long passwordId) {
        return ResponseEntity.ok(passwordService.getSharedPasswordById(passwordId));
    }

    @DeleteMapping("/shared-passwords/{passwordId}")
    public ResponseEntity<?> removePasswordSharing(@PathVariable Long passwordId) {
        passwordService.removePasswordSharing(passwordId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/password-key-verification")
    public ResponseEntity<?> checkIfPasswordKeyValid(@RequestBody String passwordKey) {
        return ResponseEntity.ok(passwordService.checkIfPasswordKeyValidForAll(passwordKey));
    }

    @GetMapping("/password-types")
    public ResponseEntity<List<PasswordTypeDto>> getPasswordTypes() {
        return ResponseEntity.ok(passwordService.getPasswordTypes());
    }
}
