package com.example.passwordwallet.auth.repositories;

import com.example.passwordwallet.domain.User;
import com.example.passwordwallet.domain.UserLoginInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLoginInfoRepository extends JpaRepository<UserLoginInfo, Long> {

    Optional<UserLoginInfo> findByUserId(Long userId);
}
