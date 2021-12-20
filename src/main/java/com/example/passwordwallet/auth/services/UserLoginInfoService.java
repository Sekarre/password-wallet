package com.example.passwordwallet.auth.services;


import com.example.passwordwallet.domain.User;
import com.example.passwordwallet.domain.UserLoginInfo;

public interface UserLoginInfoService {

    UserLoginInfo saveLoginInfo(boolean loginSuccessful, User user);

    void save(UserLoginInfo userLoginInfo);
}
