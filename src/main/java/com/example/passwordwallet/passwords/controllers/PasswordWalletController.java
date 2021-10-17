package com.example.passwordwallet.passwords.controllers;

import com.example.passwordwallet.passwords.dto.PasswordDto;
import com.example.passwordwallet.passwords.dto.PasswordTypeDto;
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
    public ResponseEntity<?> addNewPassword(@RequestBody @Valid PasswordDto passwordDto) {
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

    @GetMapping("/password-types")
    public ResponseEntity<List<PasswordTypeDto>> getPasswordTypes() {
        return ResponseEntity.ok(passwordService.getPasswordTypes());
    }
}
