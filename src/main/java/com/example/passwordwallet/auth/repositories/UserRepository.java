package com.example.passwordwallet.auth.repositories;

import com.example.passwordwallet.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginAndPassword(String login, String password);

    Optional<User> findByLogin(String login);

    Optional<User> findByEmail(String email);
}
