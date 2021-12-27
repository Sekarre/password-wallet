package com.example.passwordwallet.passwords.repositories;

import com.example.passwordwallet.domain.SharedPassword;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SharedPasswordRepository extends JpaRepository<SharedPassword, Long> {

    List<SharedPassword> findAllBySharedToUserId(Long userId, Pageable pageable);

    List<SharedPassword> findAllByOwnerUserId(Long ownerId, Pageable pageable);

    Optional<SharedPassword> findById(Long id);

    List<SharedPassword> findAllByOriginalPasswordId(Long originalPasswordId);

    void deleteAllByOriginalPasswordId(Long originalPasswordId);

    boolean existsByOriginalPasswordIdAndSharedToUserId(Long originalPasswordId, Long sharedToUserId);
}
