package com.example.passwordwallet.auth.controllers;

import com.example.passwordwallet.TestUtil;
import com.example.passwordwallet.auth.dto.*;
import com.example.passwordwallet.auth.services.AuthService;
import com.example.passwordwallet.factories.UserMockFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest {

    @Mock
    AuthService authService;

    MockMvc mockMvc;

    AuthController authController;

    private static final String BASE_URL = "/api/auth/";
    TokenResponse tokenResponse = new TokenResponse("token");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(authService);

        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void should_generate_token() throws Exception {
        //given
        when(authService.getToken(any())).thenReturn(tokenResponse);
        UserCredentials userCredentials = UserMockFactory.buildUserCredentialsMock();
        byte[] content = TestUtil.convertObjectToJsonBytes(userCredentials);

        //when + then
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value(tokenResponse.getToken()));
        verify(authService, times(1)).getToken(any());
    }

    @Test
    void should_set_password_key() throws Exception {
        //given
        when(authService.setPasswordKey(any())).thenReturn(tokenResponse);
        UserPasswordKeyDto userPasswordKeyDto = UserMockFactory.buildUserPasswordKeyDtoMock();
        byte[] content = TestUtil.convertObjectToJsonBytes(userPasswordKeyDto);

        //when + then
        mockMvc.perform(post(BASE_URL + "key")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value(tokenResponse.getToken()));
        verify(authService, times(1)).setPasswordKey(any());
    }

    @Test
    void should_change_password() throws Exception {
        //given
        when(authService.changePassword(any())).thenReturn(tokenResponse);
        UserPasswordChangeDto userPasswordChangeDto = UserMockFactory.buildUserPasswordChangeDtoMock();
        byte[] content = TestUtil.convertObjectToJsonBytes(userPasswordChangeDto);

        //when + then
        mockMvc.perform(post(BASE_URL + "password-change")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value(tokenResponse.getToken()));
        verify(authService, times(1)).changePassword(any());
    }

    @Test
    void should_create_new_account() throws Exception {
        //given
        when(authService.createNewAccount(any())).thenReturn(tokenResponse);
        UserDto userDto = UserMockFactory.buildUserDtoMock();
        byte[] content = TestUtil.convertObjectToJsonBytes(userDto);

        //when + then
        mockMvc.perform(post(BASE_URL + "new-account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value(tokenResponse.getToken()));
        verify(authService, times(1)).createNewAccount(any());
    }

    @Test
    void should_get_user_data() throws Exception {
        //given
        UserDto userDto = UserMockFactory.buildUserDtoMock();
        when(authService.getUserData()).thenReturn(userDto);

        //when + then
        mockMvc.perform(get(BASE_URL + "user-data"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value(userDto.getLogin()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value(userDto.getPassword()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.passwordType").value(userDto.getPasswordType().name()));

        verify(authService, times(1)).getUserData();
    }
}