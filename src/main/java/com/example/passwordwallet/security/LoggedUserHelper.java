package com.example.passwordwallet.security;

import com.example.passwordwallet.domain.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class LoggedUserHelper {
    public static User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
