package com.example.passwordwallet.auth.services.impl;

import com.example.passwordwallet.auth.dto.*;
import com.example.passwordwallet.auth.exceptions.BadCredentialException;
import com.example.passwordwallet.auth.mappers.UserLoginInfoMapper;
import com.example.passwordwallet.auth.mappers.UserMapper;
import com.example.passwordwallet.auth.repositories.UserRepository;
import com.example.passwordwallet.auth.services.AccountLockService;
import com.example.passwordwallet.auth.services.AuthService;
import com.example.passwordwallet.auth.services.UserLoginEventService;
import com.example.passwordwallet.auth.services.UserLoginInfoService;
import com.example.passwordwallet.domain.UserLoginInfo;
import com.example.passwordwallet.domain.enums.PasswordType;
import com.example.passwordwallet.domain.User;
import com.example.passwordwallet.domain.helper.UserLoginEventByIp;
import com.example.passwordwallet.security.JwtTokenUtil;
import com.example.passwordwallet.security.LoggedUserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.passwordwallet.security.LoggedUserHelper.getCurrentUser;
import static com.example.passwordwallet.util.HashUtil.generateRandomSalt;
import static com.example.passwordwallet.util.HashUtil.hashUserPassword;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserLoginInfoService userLoginInfoService;
    private final UserLoginEventService userLoginEventService;
    private final AccountLockService accountLockService;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserMapper userMapper;
    private final UserLoginInfoMapper userLoginInfoMapper;

    @Override
    public TokenResponse getToken(UserCredentials userCredentials) {
        User authenticatedUser = getAuthenticatedUser(userCredentials);

        return new TokenResponse(jwtTokenUtil.generateAccessToken(authenticatedUser));
    }

    @Override
    public User getAuthenticatedUser(UserCredentials userCredentials) {
        User user = getUserByLogin(userCredentials.getLogin());

        accountLockService.checkIfAccountLocked(user);
        accountLockService.checkIfValidIpAddress(user);

        UserLoginInfo userLoginInfo;
        Optional<User> optionalUser = userRepository.findByLoginAndPassword(userCredentials.getLogin(), hashUserPassword(user, userCredentials.getPassword()));

        if (optionalUser.isEmpty()) {
            userLoginInfo = userLoginInfoService.saveLoginInfo(false, user);
            accountLockService.lockAccountIfNeeded(user, userLoginInfo);

            throw new BadCredentialException("Bad credentials");
        }

        userLoginInfo = userLoginInfoService.saveLoginInfo(true, user);
        accountLockService.lockAccountIfNeeded(user, userLoginInfo);

        return optionalUser.get();
    }

    @Override
    public TokenResponse setPasswordKey(UserPasswordKeyDto userPasswordKeyDto) {
        User currentUser = getCurrentUser();

        return new TokenResponse(jwtTokenUtil.generateAccessTokenWithKey(currentUser, userPasswordKeyDto.getPasswordKey()));
    }

    @Override
    public TokenResponse changePassword(UserPasswordChangeDto userPasswordChangeDto) {
        User currentUser = getAuthenticatedUser(
                new UserCredentials(getCurrentUser().getLogin(), userPasswordChangeDto.getCurrentPassword()));
        currentUser.setPasswordType(userPasswordChangeDto.getPasswordType());

        User updatedUser = updateUserPassword(currentUser, userPasswordChangeDto.getNewPassword());

        return new TokenResponse(jwtTokenUtil.generateAccessToken(updatedUser));
    }

    @Override
    public TokenResponse createNewAccount(UserDto userDto) {
        User newUser = userMapper.mapUserDtoToUser(userDto);
        String password = userDto.getPassword();
        newUser.setSalt(generateRandomSalt());
        newUser.setPassword(hashUserPassword(newUser, password));

        User createdUser = userRepository.save(newUser);

        return new TokenResponse(jwtTokenUtil.generateAccessToken(createdUser));
    }

    @Override
    public UserDto getUserData() {
        User currentUser = getCurrentUser();

        return userMapper.mapUserToUserDto(currentUser);
    }

    @Override
    public UserLoginInfoDto getLoginInfoData() {
        User currentUser = getCurrentUser();

        return userLoginInfoMapper.mapToSummarizedUserLoginInfoDto(currentUser);
    }

    @Override
    public List<UserLoginEventByIpDto> getUserLoginEventsByIp() {
        User currentUser = getCurrentUser();

        return userLoginEventService.getUserLoginEventsByIp(currentUser).stream()
                .map(this::getUserLoginEventByIpDto)
                .collect(Collectors.toList());
    }

    @Override
    public void banUserIp(String userIp) {
        accountLockService.banIpAddress(getCurrentUser(), userIp);
    }

    @Override
    public void unbanUserIp(String userIp) {
        accountLockService.unbanIpAddress(getCurrentUser(), userIp);
    }

    private UserLoginEventByIpDto getUserLoginEventByIpDto(UserLoginEventByIp userLoginEventByIp) {
        return UserLoginEventByIpDto.builder()
                .ip(userLoginEventByIp.getIp())
                .userId(userLoginEventByIp.getUserId())
                .loginCount(userLoginEventByIp.getLoginCount())
                .isBanned(accountLockService.isIpBanned(getCurrentUser(), userLoginEventByIp.getIp()))
                .build();
    }

    private User getUserByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new BadCredentialException("Bad credentials"));
    }

    private User updateUserPassword(User user, String password) {
        user.setSalt(PasswordType.SHA512.equals(user.getPasswordType()) ? generateRandomSalt() : null);
        user.setPassword(hashUserPassword(user, password));

        return userRepository.save(user);
    }
}
