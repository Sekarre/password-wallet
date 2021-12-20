package com.example.passwordwallet.auth.services.impl;

import com.example.passwordwallet.auth.repositories.UserLoginInfoRepository;
import com.example.passwordwallet.auth.services.UserLoginEventService;
import com.example.passwordwallet.auth.services.UserLoginInfoService;
import com.example.passwordwallet.domain.User;
import com.example.passwordwallet.domain.UserLoginEvent;
import com.example.passwordwallet.domain.UserLoginInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserLoginInfoServiceImpl implements UserLoginInfoService {

    private final UserLoginEventService userLoginEventService;
    private final UserLoginInfoRepository userLoginInfoRepository;

    @Override
    public UserLoginInfo saveLoginInfo(boolean loginSuccessful, User user) {
        UserLoginEvent userLoginEvent = userLoginEventService.createUserLoginEvent(loginSuccessful, user);

        return userLoginInfoRepository.findByUserId(user.getId())
                .map(userLoginInfo -> userLoginInfoRepository.save(updateUserLoginInfo(userLoginInfo, userLoginEvent, loginSuccessful)))
                .orElseGet(() -> userLoginInfoRepository.save(createUserLoginInfo(user, userLoginEvent, loginSuccessful)));
    }

    @Override
    public void save(UserLoginInfo userLoginInfo) {
        userLoginInfoRepository.save(userLoginInfo);
    }

    private UserLoginInfo updateUserLoginInfo(UserLoginInfo userLoginInfo, UserLoginEvent userLoginEvent, boolean loginSuccessful) {
        userLoginInfo.setLastLoginDate(LocalDateTime.now());
        userLoginInfo.setLastSuccessfulLoginDate(loginSuccessful ? LocalDateTime.now() : userLoginInfo.getLastSuccessfulLoginDate());
        userLoginInfo.getUserLoginEvents().add(userLoginEvent);
        userLoginInfo.setFailureCount(loginSuccessful ? 0 : (userLoginInfo.getFailureCount() + 1));
        userLoginEvent.setUserLoginInfo(userLoginInfo);
        return userLoginInfo;
    }

    private UserLoginInfo createUserLoginInfo(User user, UserLoginEvent userLoginEvent, boolean loginSuccessful) {
        UserLoginInfo userLoginInfo = UserLoginInfo.builder()
                .user(user)
                .failureCount(loginSuccessful ? 0 : 1)
                .lastLoginDate(LocalDateTime.now())
                .userLoginEvents(new ArrayList<>() {{
                    add(userLoginEvent);
                }})
                .lastSuccessfulLoginDate(loginSuccessful ? LocalDateTime.now() : null)
                .build();

        userLoginEvent.setUserLoginInfo(userLoginInfo);

        return userLoginInfo;
    }
}
