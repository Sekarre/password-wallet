package com.example.passwordwallet.passwords.mappers;

import com.example.passwordwallet.domain.Password;
import com.example.passwordwallet.passwords.dto.PasswordCreateDto;
import com.example.passwordwallet.passwords.dto.PasswordDto;
import com.example.passwordwallet.security.LoggedUserHelper;
import com.example.passwordwallet.util.EncryptionUtil;
import com.example.passwordwallet.util.FakePasswordFactory;
import org.mapstruct.*;

@Mapper(builder = @Builder(disableBuilder = true))
public abstract class PasswordMapper {

    @BeanMapping(qualifiedByName = "randomizePassword")
    public abstract PasswordDto mapPasswordToPasswordDto(Password password);

    @BeanMapping(qualifiedByName = "decryptPassword")
    public abstract PasswordDto mapPasswordToPasswordDtoWithPassword(Password password);

    @BeanMapping(qualifiedByName = "encryptPasswordCreate")
    public abstract Password mapPasswordDtoToPassword(PasswordCreateDto passwordDto);

    @BeanMapping(qualifiedByName = "encryptPassword")
    public abstract Password mapPasswordDtoToPasswordUpdate(PasswordDto passwordDto, @MappingTarget Password password);

    @Named("encryptPassword")
    @AfterMapping
    public void encryptPassword(PasswordDto passwordDto, @MappingTarget Password password) {
        password.setPassword(EncryptionUtil.encryptPassword(passwordDto.getPassword(), LoggedUserHelper.getCurrentUser().getKey()));
    }

    @Named("encryptPasswordCreate")
    @AfterMapping
    public void encryptPassword(PasswordCreateDto passwordDto, @MappingTarget Password password) {
        password.setPassword(EncryptionUtil.encryptPassword(passwordDto.getPassword(), LoggedUserHelper.getCurrentUser().getKey()));
    }

    @Named("decryptPassword")
    @AfterMapping
    public void decryptPassword(Password password, @MappingTarget PasswordDto passwordDto) {
        passwordDto.setPassword(EncryptionUtil.decryptPassword(password.getPassword(), LoggedUserHelper.getCurrentUser().getKey()));
    }

    @Named("randomizePassword")
    @AfterMapping
    public void randomizePassword(Password password, @MappingTarget PasswordDto passwordDto) {
        passwordDto.setPassword(FakePasswordFactory.buildRandomFakePassword());
    }
}
