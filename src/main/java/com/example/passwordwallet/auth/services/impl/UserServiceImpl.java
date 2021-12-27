package com.example.passwordwallet.auth.services.impl;

import com.example.passwordwallet.auth.repositories.UserRepository;
import com.example.passwordwallet.auth.services.UserService;
import com.example.passwordwallet.domain.User;
import com.example.passwordwallet.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public Optional<User> getOptionalUserByLoginAndPassword(String login, String password) {
        return userRepository.findByLoginAndPassword(login, password);
    }

    @Override
    public User getUserByLoginAndPassword(String login, String password) {
        return userRepository.findByLoginAndPassword(login, password).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public User save(User userToSave) {
        return userRepository.save(userToSave);
    }
}
