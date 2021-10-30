package com.example.passwordwallet.passwords.mappers;

import com.example.passwordwallet.SecurityContextMockSetup;
import com.example.passwordwallet.domain.Password;
import com.example.passwordwallet.exceptions.BadKeyException;
import com.example.passwordwallet.factories.PasswordMockFactory;
import com.example.passwordwallet.passwords.dto.PasswordCreateDto;
import com.example.passwordwallet.passwords.dto.PasswordDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class PasswordMapperTest extends SecurityContextMockSetup {

    PasswordMapper passwordMapper = Mappers.getMapper(PasswordMapper.class);

    @BeforeEach
    public void setUp() {
        super.setUpSecurityContext();
    }

    @Test
    void should_map_password_to_password_dto() {
        //given
        Password source = PasswordMockFactory.buildPasswordMock();

        //when
        PasswordDto result = passwordMapper.mapPasswordToPasswordDto(source);

        //then
        assertEquals(source.getId(), result.getId());
        assertEquals(source.getTitle(), result.getTitle());
        assertEquals(source.getLogin(), result.getLogin());
        assertNotEquals(source.getPassword(), result.getPassword());
        assertEquals(source.getWebAddress(), result.getWebAddress());
        assertEquals(source.getDescription(), result.getDescription());
    }

    @Test
    void should_not_map_password_to_password_dto_with_password() {
        //given
        Password source = PasswordMockFactory.buildPasswordMock();

        //when + then
        assertThrows(BadKeyException.class, () -> passwordMapper.mapPasswordToPasswordDtoWithPassword(source));
    }

    @Test
    void should_map_password_dto_to_password() {
        //given
        PasswordCreateDto source = PasswordMockFactory.buildPasswordCreateDtoMock();

        //when
        Password result = passwordMapper.mapPasswordDtoToPassword(source);

        //then
        assertEquals(source.getTitle(), result.getTitle());
        assertEquals(source.getLogin(), result.getLogin());
        assertNotEquals(source.getPassword(), result.getPassword());
        assertEquals(source.getWebAddress(), result.getWebAddress());
        assertEquals(source.getDescription(), result.getDescription());
    }

    @Test
    void should_map_password_dto_to_password_update() {
        //given
        PasswordDto source = PasswordMockFactory.buildPasswordDtoMock();
        Password toUpdate = PasswordMockFactory.buildPasswordMock();

        //when
        Password result = passwordMapper.mapPasswordDtoToPasswordUpdate(source, toUpdate);

        //then
        assertEquals(toUpdate.getId(), result.getId());
        assertEquals(toUpdate.getTitle(), result.getTitle());
        assertEquals(toUpdate.getLogin(), result.getLogin());
        assertEquals(toUpdate.getPassword(), result.getPassword());
        assertEquals(toUpdate.getWebAddress(), result.getWebAddress());
        assertEquals(toUpdate.getDescription(), result.getDescription());
    }
}