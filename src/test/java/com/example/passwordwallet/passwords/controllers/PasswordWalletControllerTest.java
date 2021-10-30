package com.example.passwordwallet.passwords.controllers;

import com.example.passwordwallet.TestUtil;
import com.example.passwordwallet.factories.PasswordMockFactory;
import com.example.passwordwallet.passwords.dto.PasswordCreateDto;
import com.example.passwordwallet.passwords.dto.PasswordDto;
import com.example.passwordwallet.passwords.dto.PasswordTypeDto;
import com.example.passwordwallet.passwords.services.PasswordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PasswordWalletControllerTest {

    @Mock
    PasswordService passwordService;

    MockMvc mockMvc;

    PasswordWalletController passwordWalletController;

    private static final String BASE_URL = "/api/password-wallet/";
    private final Long id = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        passwordWalletController = new PasswordWalletController(passwordService);

        mockMvc = MockMvcBuilders.standaloneSetup(passwordWalletController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void should_add_new_password() throws Exception {
        //given
        PasswordCreateDto passwordCreateDto = PasswordMockFactory.buildPasswordCreateDtoMock();
        byte[] content = TestUtil.convertObjectToJsonBytes(passwordCreateDto);

        //when + then
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk());
        verify(passwordService, times(1)).createPassword(any());
    }

    @Test
    void should_update_password() throws Exception {
        //given
        PasswordDto passwordDto = PasswordMockFactory.buildPasswordDtoMock();
        byte[] content = TestUtil.convertObjectToJsonBytes(passwordDto);

        //when + then
        mockMvc.perform(put(BASE_URL + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk());
        verify(passwordService, times(1)).updatePassword(any(), any());
    }

    @Test
    void should_get_password_by_id() throws Exception {
        //given
        PasswordDto passwordDto = PasswordMockFactory.buildPasswordDtoMock();
        when(passwordService.getPassword(any())).thenReturn(passwordDto);

        //when + then
        mockMvc.perform(get(BASE_URL + id))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(passwordDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(passwordDto.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value(passwordDto.getLogin()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value(passwordDto.getPassword()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.webAddress").value(passwordDto.getWebAddress()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(passwordDto.getDescription()));

        verify(passwordService, times(1)).getPassword(id);
    }

    @Test
    void should_get_all_passwords() throws Exception {
        //given
        PasswordDto passwordDto = PasswordMockFactory.buildPasswordDtoMock();
        when(passwordService.getAllPasswords(any())).thenReturn(List.of(passwordDto));

        //when + then
        mockMvc.perform(get(BASE_URL).param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(passwordDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(passwordDto.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].login").value(passwordDto.getLogin()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].password").value(passwordDto.getPassword()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].webAddress").value(passwordDto.getWebAddress()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(passwordDto.getDescription()));

        verify(passwordService, times(1)).getAllPasswords(any());
    }

    @Test
    void should_delete_password_by_id() throws Exception {
        //when + then
        mockMvc.perform(delete(BASE_URL + id))
                .andExpect(status().isNoContent());

        verify(passwordService, times(1)).deletePassword(id);
    }

    @Test
    void should_check_if_password_key_valid_and_return_true() throws Exception {
        //given
        String passwordKey = "key";
        byte[] content = TestUtil.convertObjectToJsonBytes(passwordKey);

        //when + then
        mockMvc.perform(post(BASE_URL + "password-key-verification")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk());
        verify(passwordService, times(1)).checkIfPasswordKeyValidForAll(any());
    }

    @Test
    void should_get_password_types() throws Exception {
        //given
        PasswordTypeDto passwordTypeDto = PasswordMockFactory.buildPasswordTypeDtoMock();
        when(passwordService.getPasswordTypes()).thenReturn(List.of(passwordTypeDto));

        //when + then
        mockMvc.perform(get(BASE_URL + "/password-types"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(passwordTypeDto.getName()));

        verify(passwordService, times(1)).getPasswordTypes();
    }
}