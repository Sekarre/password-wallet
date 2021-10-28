package com.example.passwordwallet;

import com.example.passwordwallet.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.example.passwordwallet.factories.UserMockFactory.buildDefaultUserMock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SecurityContextMockSetup {

    public void setUp() {
        User user = buildDefaultUserMock();
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);
    }
}
