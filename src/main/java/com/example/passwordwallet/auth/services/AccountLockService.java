package com.example.passwordwallet.auth.services;

import com.example.passwordwallet.domain.User;
import com.example.passwordwallet.domain.UserLoginInfo;

public interface AccountLockService {

    void checkIfAccountLocked(User user);

    void lockAccountIfNeeded(User user, UserLoginInfo userLoginInfo);

    void checkIfValidIpAddress(User user);

    boolean isIpBanned(User user, String userIp);

    void banIpAddress(User user, String ipAddress);

    void unbanIpAddress(User user, String userIp);
}
