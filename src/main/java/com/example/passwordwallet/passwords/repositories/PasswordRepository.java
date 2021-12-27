package com.example.passwordwallet.passwords.repositories;

import com.example.passwordwallet.domain.Password;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordRepository extends JpaRepository<Password, Long> {

    boolean existsByIdAndUserId(Long id, Long userId);

    Optional<Password> findByIdAndUserId(Long id, Long userId);

    Page<Password> findAllByUserId(Long userId, Pageable pageable);

    List<Password> findAllByUserId(Long userId);
}
