package com.example.passwordwallet.auth.services;

import com.example.passwordwallet.SecurityContextMockSetup;
import com.example.passwordwallet.auth.dto.*;
import com.example.passwordwallet.auth.mappers.UserMapper;
import com.example.passwordwallet.auth.repositories.UserRepository;
import com.example.passwordwallet.auth.services.impl.AuthServiceImpl;
import com.example.passwordwallet.domain.PasswordType;
import com.example.passwordwallet.domain.User;
import com.example.passwordwallet.security.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.example.passwordwallet.factories.UserMockFactory.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest extends SecurityContextMockSetup {

    @Mock
    UserRepository userRepository;

    @Mock
    JwtTokenUtil jwtTokenUtil;

    @Mock
    UserMapper userMapper;

    AuthService authService;

    User user;
    String tokenValue = "tokenValue";

    @BeforeEach
    public void setUpSecurityContext() {
        super.setUpSecurityContext();
        MockitoAnnotations.openMocks(this);
        authService = new AuthServiceImpl(userRepository, jwtTokenUtil, userMapper);

        user = buildDefaultUserMock();
    }

    @Test
    void should_get_token() {
        //given
        UserCredentials userCredentials = buildUserCredentialsMock();
        when(userRepository.findByLoginAndPassword(any(), any())).thenReturn(java.util.Optional.ofNullable(user));
        when(userRepository.findByLogin(any())).thenReturn(java.util.Optional.ofNullable(user));

        //when
        TokenResponse token = authService.getToken(userCredentials);

        //then
        assertNotNull(token);
        verify(userRepository, times(1)).findByLogin(userCredentials.getLogin());
        verify(userRepository, times(1)).findByLoginAndPassword(any(), any());
    }

    @Test
    void should_get_authenticated_user_if_correct_user_credentials() {
        //given
        UserCredentials userCredentials = buildUserCredentialsMock();
        when(userRepository.findByLoginAndPassword(any(), any())).thenReturn(java.util.Optional.ofNullable(user));
        when(userRepository.findByLogin(any())).thenReturn(java.util.Optional.ofNullable(user));

        //when
        User authenticatedUser = authService.getAuthenticatedUser(userCredentials);

        //then
        assertNotNull(authenticatedUser);
        assertEquals(user, authenticatedUser);
        verify(userRepository, times(1)).findByLogin(userCredentials.getLogin());
        verify(userRepository, times(1)).findByLoginAndPassword(any(), any());
    }

    @Test
    void should_set_password_key_if_user_logged_in() {
        //given
        UserPasswordKeyDto userPasswordKeyDto = UserPasswordKeyDto.builder().passwordKey("key").build();
        when(jwtTokenUtil.generateAccessTokenWithKey(any(), any())).thenReturn(tokenValue);

        //when
        TokenResponse tokenResponse = authService.setPasswordKey(userPasswordKeyDto);

        //then
        assertNotNull(tokenResponse);
        assertEquals(tokenValue, tokenResponse.getToken());
        verify(jwtTokenUtil, times(1)).generateAccessTokenWithKey(any(), any());
    }

    @Test
    void should_change_password() {
        //given
        UserPasswordChangeDto userPasswordChangeDto = UserPasswordChangeDto.builder()
                .passwordType(PasswordType.SHA512)
                .newPassword("password2")
                .currentPassword("password")
                .build();
        when(userRepository.findByLogin(any())).thenReturn(java.util.Optional.ofNullable(user));
        when(userRepository.findByLoginAndPassword(any(), any())).thenReturn(java.util.Optional.ofNullable(user));
        when(jwtTokenUtil.generateAccessToken(any())).thenReturn(tokenValue);

        //when
        TokenResponse token = authService.changePassword(userPasswordChangeDto);

        //then
        assertNotNull(token);
        verify(userRepository, times(1)).findByLoginAndPassword(any(), any());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void should_create_new_account() {
        //given
        UserDto userDto = buildUserDtoMock();
        when(jwtTokenUtil.generateAccessToken(any())).thenReturn(tokenValue);
        when(userMapper.mapUserDtoToUser(any())).thenReturn(user);
        when(userRepository.save(any())).thenReturn(user);

        //when
        TokenResponse token = authService.createNewAccount(userDto);

        //then
        assertNotNull(token);
        verify(userRepository, times(1)).save(user);
        verify(jwtTokenUtil, times(1)).generateAccessToken(user);
    }

    @Test
    void should_get_user_data() {
        //given
        UserDto userDto = buildUserDtoMock();
        when(userMapper.mapUserToUserDto(any())).thenReturn(userDto);

        //when
        UserDto userData = authService.getUserData();

        //then
        assertNotNull(userData);
        assertEquals(userDto, userData);
    }
}