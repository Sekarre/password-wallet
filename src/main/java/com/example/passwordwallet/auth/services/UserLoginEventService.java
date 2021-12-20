package com.example.passwordwallet.auth.services;

import com.example.passwordwallet.domain.User;
import com.example.passwordwallet.domain.UserLoginEvent;
import com.example.passwordwallet.domain.helper.UserLoginEventByIp;

import java.util.List;

public interface UserLoginEventService {

    UserLoginEvent createUserLoginEvent(boolean loginSuccessful, User user);

    List<UserLoginEventByIp> getUserLoginEventsByIp(User user);
}
