package com.example.passwordwallet.passwords.mappers;

import com.example.passwordwallet.domain.SharedPassword;
import com.example.passwordwallet.passwords.dto.SharedPasswordDto;
import com.example.passwordwallet.security.LoggedUserHelper;
import com.example.passwordwallet.util.EncryptionUtil;
import com.example.passwordwallet.util.FakePasswordFactory;
import org.mapstruct.*;

@Mapper(builder = @Builder(disableBuilder = true))
public abstract class SharedPasswordMapper {

    @BeanMapping(qualifiedByName = "randomizePassword")
    @Mapping(source = "sharedToUser.email", target = "sharedToUserEmail")
    public abstract SharedPasswordDto mapSharedPasswordToSharedPasswordDto(SharedPassword sharedPassword);

    @BeanMapping(qualifiedByName = "decryptPassword")
    @Mapping(source = "sharedToUser.email", target = "sharedToUserEmail")
    public abstract SharedPasswordDto mapSharedPasswordToSharedPasswordDtoWithPassword(SharedPassword sharedPassword);

    @Named("decryptPassword")
    @AfterMapping
    public void decryptPassword(SharedPassword sharedPassword, @MappingTarget SharedPasswordDto sharedPasswordDto) {
        sharedPasswordDto.setPassword(EncryptionUtil.decryptPassword(sharedPassword.getPassword(), LoggedUserHelper.getCurrentUser().getPassword()));
    }

    @Named("randomizePassword")
    @AfterMapping
    public void randomizePassword(SharedPassword sharedPassword, @MappingTarget SharedPasswordDto SharedPasswordDto) {
        SharedPasswordDto.setPassword(FakePasswordFactory.buildRandomFakePassword());
    }
}
