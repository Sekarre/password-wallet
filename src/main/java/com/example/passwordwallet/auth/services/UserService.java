package com.example.passwordwallet.auth.services;

import com.example.passwordwallet.domain.User;

import java.util.Optional;

public interface UserService {

    User getUserById(Long id);

    User getUserByEmail(String email);

    User getUserByLogin(String login);

    Optional<User> getOptionalUserByLoginAndPassword(String login, String password);

    User getUserByLoginAndPassword(String login, String password);

    User save(User userToSave);
}
