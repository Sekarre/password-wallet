package com.example.passwordwallet.auth.services.impl;

import com.example.passwordwallet.auth.repositories.UserLoginEventRepository;
import com.example.passwordwallet.auth.services.UserLoginEventService;
import com.example.passwordwallet.domain.User;
import com.example.passwordwallet.domain.UserLoginEvent;
import com.example.passwordwallet.domain.helper.UserLoginEventByIp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.passwordwallet.util.HttpReqRespUtils.getClientIpAddressIfServletRequestExist;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserLoginEventServiceImpl implements UserLoginEventService {

    private final UserLoginEventRepository userLoginEventRepository;

    @Override
    public UserLoginEvent createUserLoginEvent(boolean loginSuccessful, User user) {
        return userLoginEventRepository.save(UserLoginEvent.builder()
                .loginDate(LocalDateTime.now())
                .userId(user.getId())
                .loginSuccessful(loginSuccessful)
                .ipAddress(getClientIpAddressIfServletRequestExist())
                .build());
    }

    @Override
    public List<UserLoginEventByIp> getUserLoginEventsByIp(User user) {
        return userLoginEventRepository.findUserLoginsCountByIp(user.getId());
    }
}
