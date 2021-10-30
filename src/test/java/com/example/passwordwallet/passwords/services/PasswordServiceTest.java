package com.example.passwordwallet.passwords.services;

import com.example.passwordwallet.SecurityContextMockSetup;
import com.example.passwordwallet.domain.Password;
import com.example.passwordwallet.domain.PasswordType;
import com.example.passwordwallet.domain.User;
import com.example.passwordwallet.exceptions.BadKeyException;
import com.example.passwordwallet.factories.PasswordMockFactory;
import com.example.passwordwallet.factories.UserMockFactory;
import com.example.passwordwallet.passwords.dto.PasswordCreateDto;
import com.example.passwordwallet.passwords.dto.PasswordDto;
import com.example.passwordwallet.passwords.dto.PasswordTypeDto;
import com.example.passwordwallet.passwords.mappers.PasswordMapper;
import com.example.passwordwallet.passwords.repositories.PasswordRepository;
import com.example.passwordwallet.passwords.services.impl.PasswordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PasswordServiceTest extends SecurityContextMockSetup {

    @Mock
    PasswordRepository passwordRepository;

    @Mock
    PasswordMapper passwordMapper;

    @Mock
    PasswordKeyValidatorService passwordKeyValidatorService;

    private PasswordService passwordService;

    private Password password;

    @BeforeEach
    public void setUpSecurityContext() {
        super.setUpSecurityContext();
        MockitoAnnotations.openMocks(this);

        password = PasswordMockFactory.buildPasswordMock();
        passwordService = new PasswordServiceImpl(passwordRepository, passwordMapper, passwordKeyValidatorService);
    }

    @Test
    void should_create_password() {
        //given
        PasswordCreateDto passwordCreateDto = PasswordMockFactory.buildPasswordCreateDtoMock();
        when(passwordMapper.mapPasswordDtoToPassword(any())).thenReturn(password);
        when(passwordRepository.save(any())).thenReturn(password);

        //when
        Password result = passwordService.createPassword(passwordCreateDto);

        //then
        assertNotNull(result);
        verify(passwordRepository, times(1)).save(any());
        verify(passwordMapper, times(1)).mapPasswordDtoToPassword(passwordCreateDto);
    }

    @Test
    void should_throw_exception_when_creating_password_if_key_not_set() {
        //given
        User user = UserMockFactory.buildDefaultUserMock();
        user.setKey(null);
        setUpSecurityContext(user);

        //when + then
        assertThrows(IllegalStateException.class, () -> passwordService.createPassword(new PasswordCreateDto()));
    }

    @Test
    void should_update_password() {
        //given
        PasswordDto passwordDto = PasswordMockFactory.buildPasswordDtoMock();
        when(passwordRepository.findByIdAndUserId(any(), any())).thenReturn(java.util.Optional.ofNullable(password));
        when(passwordMapper.mapPasswordDtoToPasswordUpdate(any(), any())).thenReturn(password);
        when(passwordRepository.save(any())).thenReturn(password);
        when(passwordKeyValidatorService.isKeyValid(any(), any())).thenReturn(true);

        //when
        Password result = passwordService.updatePassword(1L, passwordDto);

        //then
        assertEquals(password, result);
        verify(passwordRepository, times(1)).findByIdAndUserId(any(), any());
        verify(passwordMapper, times(1)).mapPasswordDtoToPasswordUpdate(passwordDto, password);
        verify(passwordRepository, times(1)).save(password);
    }

    @Test
    void should_throw_exception_when_updating_password_if_key_NOT_valid() {
        //given
        PasswordDto passwordDto = PasswordMockFactory.buildPasswordDtoMock();
        when(passwordRepository.findByIdAndUserId(any(), any())).thenReturn(java.util.Optional.ofNullable(PasswordMockFactory.buildPasswordMock()));
        when(passwordKeyValidatorService.isKeyValid(any(), any())).thenReturn(false);

        //when + then
        assertThrows(BadKeyException.class, () -> passwordService.updatePassword(1L, passwordDto));

        verify(passwordRepository, times(1)).findByIdAndUserId(any(), any());
        verify(passwordMapper, times(0)).mapPasswordDtoToPasswordUpdate(any(), any());
        verify(passwordRepository, times(0)).save(any());
    }

    @Test
    void should_get_password() {
        //given
        PasswordDto passwordDto = PasswordMockFactory.buildPasswordDtoMock();
        when(passwordRepository.findByIdAndUserId(any(), any())).thenReturn(java.util.Optional.ofNullable(password));
        when(passwordMapper.mapPasswordToPasswordDtoWithPassword(any())).thenReturn(passwordDto);
        when(passwordKeyValidatorService.isKeyValid(any(), any())).thenReturn(true);

        //when
        PasswordDto result = passwordService.getPassword(1L);

        //then
        assertEquals(passwordDto, result);
        verify(passwordRepository, times(1)).findByIdAndUserId(any(), any());
        verify(passwordMapper, times(1)).mapPasswordToPasswordDtoWithPassword(password);
        verify(passwordKeyValidatorService, times(1)).isKeyValid(any(), any());
    }

    @Test
    void should_get_all_passwords() {
        //given
        PasswordDto passwordDto = PasswordMockFactory.buildPasswordDtoMock();
        when(passwordRepository.findAllByUserId(any(), any())).thenReturn(new PageImpl<>(List.of(password)));
        when(passwordMapper.mapPasswordToPasswordDto(any())).thenReturn(passwordDto);

        //when
        List<PasswordDto> resultList = passwordService.getAllPasswords(Pageable.unpaged());

        //then
        assertEquals(1, resultList.size());
        assertEquals(passwordDto, resultList.get(0));
        verify(passwordRepository, times(1)).findAllByUserId(any(), any());
        verify(passwordMapper, times(1)).mapPasswordToPasswordDto(password);
    }

    @Test
    void should_check_if_password_key_valid_for_all_and_return_true() {
        //given
        String key = "key";
        PasswordDto passwordDto = PasswordMockFactory.buildPasswordDtoMock();
        when(passwordRepository.findAllByUserId(any())).thenReturn(List.of(password));
        when(passwordMapper.mapPasswordToPasswordDto(any())).thenReturn(passwordDto);
        when(passwordKeyValidatorService.isKeyValid(any(), any())).thenReturn(true);

        //when
        boolean result = passwordService.checkIfPasswordKeyValidForAll(key);

        //then
        assertTrue(result);
        verify(passwordRepository, times(1)).findAllByUserId(any());
        verify(passwordKeyValidatorService, times(1)).isKeyValid(password, key);
    }

    @Test
    void should_throw_exception_when_checking_if_password_key_valid_for_all() {
        //given
        String key = "key";
        PasswordDto passwordDto = PasswordMockFactory.buildPasswordDtoMock();
        when(passwordRepository.findAllByUserId(any())).thenReturn(List.of(password));
        when(passwordMapper.mapPasswordToPasswordDto(any())).thenReturn(passwordDto);
        when(passwordKeyValidatorService.isKeyValid(any(), any())).thenReturn(false);

        //when + then
        assertThrows(BadKeyException.class, () -> passwordService.checkIfPasswordKeyValidForAll(key));

        verify(passwordRepository, times(1)).findAllByUserId(any());
        verify(passwordKeyValidatorService, times(1)).isKeyValid(password, key);
    }

    @Test
    void should_delete_password() {
        //given
        when(passwordRepository.findByIdAndUserId(any(), any())).thenReturn(java.util.Optional.ofNullable(password));
        when(passwordKeyValidatorService.isKeyValid(any(), any())).thenReturn(true);

        //when
        passwordService.deletePassword(1L);

        //then
        verify(passwordRepository, times(1)).findByIdAndUserId(any(), any());
        verify(passwordRepository, times(1)).delete(password);
        verify(passwordKeyValidatorService, times(1)).isKeyValid(any(), any());
    }

    @Test
    void should_update_password_with_new_user() {
        //given
        User user = UserMockFactory.buildDefaultUserMock();
        when(passwordRepository.findAllByUserId(any())).thenReturn(List.of(password));

        //when
        passwordService.updatePasswordsWithNewUser(user);

        //then
        verify(passwordRepository, times(1)).saveAll(any());
        verify(passwordRepository, times(1)).findAllByUserId(user.getId());
    }

    @Test
    void should_get_password_types() {
        //given
        List<PasswordType> passwordTypes = Arrays.asList(PasswordType.values());

        //when
        List<PasswordTypeDto> result = passwordService.getPasswordTypes();

        //then
        assertEquals(passwordTypes.size(), result.size());
    }
}