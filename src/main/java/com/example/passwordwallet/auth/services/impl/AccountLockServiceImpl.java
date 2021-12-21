package com.example.passwordwallet.auth.services.impl;

import com.example.passwordwallet.auth.repositories.BannedIpAddressesRepository;
import com.example.passwordwallet.auth.services.AccountLockService;
import com.example.passwordwallet.auth.services.UserLoginInfoService;
import com.example.passwordwallet.domain.BannedIpAddress;
import com.example.passwordwallet.domain.User;
import com.example.passwordwallet.domain.UserLoginInfo;
import com.example.passwordwallet.domain.enums.LockTime;
import com.example.passwordwallet.exceptions.NotFoundException;
import com.example.passwordwallet.util.HttpReqRespUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountLockServiceImpl implements AccountLockService {

    private final UserLoginInfoService userLoginInfoService;
    private final BannedIpAddressesRepository bannedIpAddressesRepository;

    @Override
    public void checkIfAccountLocked(User user) {

        if (Objects.isNull(user) || Objects.isNull(user.getUserLoginInfo())) {
            return;
        }

        LocalDateTime currentDateTime = LocalDateTime.now();
        if (user.getUserLoginInfo().getLockedTo().isAfter(currentDateTime)) {
            throw new IllegalStateException("Account is locked for " + ChronoUnit.SECONDS.between(currentDateTime, user.getUserLoginInfo().getLockedTo()) + " seconds");
        }
    }

    @Override
    public void checkIfValidIpAddress(User user) {
        if (isIpBanned(user, HttpReqRespUtils.getClientIpAddressIfServletRequestExist())) {
            throw new IllegalStateException("Your ip address has been blocked by user");
        }
    }

    @Override
    public void lockAccountIfNeeded(User user, UserLoginInfo userLoginInfo) {
        Map<Integer, Integer> lockSecondsPerFailureCount =
                Arrays.stream(LockTime.values())
                        .collect(Collectors.toMap(LockTime::getFailureCount, LockTime::getTimeInSeconds));
        Integer secondsToLock = lockSecondsPerFailureCount.get(userLoginInfo.getFailureCount());

        if (Objects.isNull(secondsToLock)) {
            secondsToLock = lockSecondsPerFailureCount.get(LockTime.SAYONARA.getFailureCount());
        }

        LocalDateTime dateToLockAcc = LocalDateTime.now().plusSeconds(secondsToLock);

        userLoginInfo.setLockedTo(dateToLockAcc);
        userLoginInfoService.save(userLoginInfo);
    }

    @Override
    public boolean isIpBanned(User user, String userIp) {
        return bannedIpAddressesRepository.existsByUserIdAndAddressIp(user.getId(), userIp);
    }

    @Override
    public void banIpAddress(User user, String ipAddress) {

        if (bannedIpAddressesRepository.findByUserIdAndAddressIp(user.getId(), ipAddress).isPresent()) {
            throw new IllegalStateException("Ip address already banned");
        }

        bannedIpAddressesRepository.save(BannedIpAddress.builder()
                .addressIp(ipAddress)
                .userId(user.getId())
                .build());
    }

    @Override
    public void unbanIpAddress(User user, String userIp) {
        BannedIpAddress bannedIpAddress = bannedIpAddressesRepository.findByUserIdAndAddressIp(user.getId(), userIp)
                .orElseThrow(() -> new NotFoundException("Given address doesnt exist or is not banned"));

        bannedIpAddressesRepository.deleteById(bannedIpAddress.getId());
    }
}
