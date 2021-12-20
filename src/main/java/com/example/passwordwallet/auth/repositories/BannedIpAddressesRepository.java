package com.example.passwordwallet.auth.repositories;

import com.example.passwordwallet.domain.BannedIpAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BannedIpAddressesRepository extends JpaRepository<BannedIpAddress, Long> {

    boolean existsByUserIdAndAddressIp(Long userId, String addressIp);

    Optional<BannedIpAddress> findByUserIdAndAddressIp(Long userId, String addressIp);
}
