package com.example.passwordwallet.bootstrap;

import com.example.passwordwallet.domain.PasswordType;
import com.example.passwordwallet.domain.User;
import com.example.passwordwallet.auth.repositories.UserRepository;
import com.example.passwordwallet.util.EncryptionUtil;
import com.example.passwordwallet.util.HashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        createUsers();
        System.out.println("-------------- Data loaded --------------");
    }

    public void createUsers() {
        userRepository.save(User.builder()
                .login("user")
                .password(HashUtil.calculateSHA512("user", "user"))
                .salt("user")
                .passwordType(PasswordType.SHA512)
                .build());

        userRepository.save(User.builder()
                .login("admin")
                .password(HashUtil.calculateSHA512("admin", "admin"))
                .salt("admin")
                .passwordType(PasswordType.SHA512)
                .build());

        userRepository.save(User.builder()
                .login("guest")
                .password(HashUtil.calculateHMAC("guest", EncryptionUtil.encryptPassword("guest", "guest")))
                .salt("guest")
                .passwordType(PasswordType.HMAC)
                .build());
    }
}
