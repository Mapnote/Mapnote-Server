package com.mapnote.mapnoteserver.domain.user.service;


import static org.springframework.util.ObjectUtils.isEmpty;

import com.mapnote.mapnoteserver.domain.common.exception.BadRequestException;
import com.mapnote.mapnoteserver.domain.common.exception.ConflictException;
import com.mapnote.mapnoteserver.domain.common.exception.ErrorCode;
import com.mapnote.mapnoteserver.domain.common.exception.NotFoundException;
import com.mapnote.mapnoteserver.domain.user.dto.UserRequest;
import com.mapnote.mapnoteserver.domain.user.dto.UserRequest.ChangeInfo;
import com.mapnote.mapnoteserver.domain.user.dto.UserRequest.Email;
import com.mapnote.mapnoteserver.domain.user.dto.UserRequest.NewPassword;
import com.mapnote.mapnoteserver.domain.user.dto.UserRequest.RenewalPassword;
import com.mapnote.mapnoteserver.domain.user.dto.UserResponse;
import com.mapnote.mapnoteserver.domain.user.dto.UserResponse.UserDetailResponse;
import com.mapnote.mapnoteserver.domain.user.entity.Authority;
import com.mapnote.mapnoteserver.domain.user.entity.User;
import com.mapnote.mapnoteserver.domain.user.repository.UserRepository;
import com.mapnote.mapnoteserver.domain.user.util.PasswordEncrypter;
import com.mapnote.mapnoteserver.domain.user.util.UserConverter;
import com.mapnote.mapnoteserver.security.jwt.JwtExpiration;
import com.mapnote.mapnoteserver.security.jwt.JwtTokenProvider;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final JwtTokenProvider jwtTokenProvider;
  private final RedisTemplate redisTemplate;

  private static final String BEARER_TYPE = "Bearer";

  public UserService(UserRepository userRepository,
      AuthenticationManagerBuilder authenticationManagerBuilder,
      JwtTokenProvider jwtTokenProvider,
      RedisTemplate redisTemplate) {
    this.userRepository = userRepository;
    this.authenticationManagerBuilder = authenticationManagerBuilder;
    this.jwtTokenProvider = jwtTokenProvider;
    this.redisTemplate = redisTemplate;
  }

  @Transactional
  public UUID signUp(UserRequest.SignUp signUp) {

    User user = User.builder()
        .email(signUp.getEmail())
        .password(PasswordEncrypter.encrypt(signUp.getPassword()))
        .name(signUp.getName())
        .build();

    Authority.addUserAuth(user);

    userRepository.save(user);

    return user.getId();
  }

  @Transactional
  public UserResponse.TokenResponse login(UserRequest.Login login) {
    User user = userRepository.findByEmail(login.getEmail())
        .orElseThrow(() -> new NotFoundException("?????? ????????? ???????????? ????????????.", ErrorCode.NOT_FOUND_USER));

    if(!user.matchPassword(login.getPassword())) throw new BadRequestException("????????? ??????????????? ?????????????????????.", ErrorCode.WRONG_PASSWORD_INPUT);

//    // Login id, pw ?????? Authentication ?????? ??????
//    UsernamePasswordAuthenticationToken authenticationToken = login.toAuthentication();
//
//    // ??????
//    Authentication authentication = authenticationManagerBuilder.getObject()
//        .authenticate(authenticationToken);

    // ?????? ?????? ?????? JWT ?????? ??????
    String accessToken = jwtTokenProvider.generateAccessToken(login.getEmail());
    String refreshToken = jwtTokenProvider.generateRefreshToken(login.getEmail());

    // refreshToken redis ??? ??????
    redisTemplate.opsForValue()
        .set("RefreshToken:" + login.getEmail(), refreshToken);

    return UserResponse.TokenResponse.builder()
        .grantType(BEARER_TYPE)
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .refreshTokenExpirationTime(JwtExpiration.REFRESH_TOKEN_EXPIRATION_TIME.getExpirationTime())
        .build();
  }

  @Transactional
  public UserResponse.TokenResponse reissue(UserRequest.Reissue reissue) {

    // refresh token ?????? (????????????)
    if(!jwtTokenProvider.validateToken(reissue.getRefreshToken())) throw new BadRequestException("Refresh Token ????????? ???????????? ????????????.", ErrorCode.UNAUTHORIZED_REQUEST);

    // access Token ?????? email ??????
    String userEmail = jwtTokenProvider.getUserEmail(reissue.getAccessToken());
//    Authentication authentication = jwtTokenProvider.getAuthentication(reissue.getAccessToken());

    // Redis ?????? email??? ????????? refresh token ??? ????????????
    String refreshToken = (String) redisTemplate.opsForValue().get("RefreshToken:" + userEmail);

    // ???????????? ??? ???????????? ??????
    if(isEmpty(refreshToken)) throw new BadRequestException("Refresh Token ????????? ???????????? ????????????.", ErrorCode.UNAUTHORIZED_REQUEST);
    if(!refreshToken.equals(reissue.getRefreshToken())) throw new BadRequestException("Refresh Token ????????? ???????????? ????????????.", ErrorCode.UNAUTHORIZED_REQUEST);

    // ?????? ?????? ??????
    String newAccessToken = jwtTokenProvider.generateAccessToken(userEmail);
    String newRefreshToken = jwtTokenProvider.generateRefreshToken(userEmail);

    // ????????????
    redisTemplate.opsForValue()
        .set("RefreshToken:" + userEmail, newRefreshToken);

    return UserResponse.TokenResponse.builder()
        .grantType(BEARER_TYPE)
        .accessToken(newAccessToken)
        .refreshToken(newRefreshToken)
        .refreshTokenExpirationTime(JwtExpiration.REFRESH_TOKEN_EXPIRATION_TIME.getExpirationTime())
        .build();
  }

  @Transactional
  public void logout(UserRequest.Logout logout) {

    // token ??????
    if(!jwtTokenProvider.validateToken(logout.getAccessToken())) throw new BadRequestException("Access Token ????????? ?????????????????????.", ErrorCode.UNAUTHORIZED_REQUEST);

    // token ?????? email ??????
    String userEmail = jwtTokenProvider.getUserEmail(logout.getAccessToken());

    // redis ????????? ??????
    if(redisTemplate.opsForValue().get("RefreshToken:" + userEmail) != null) {
      redisTemplate.delete("RefreshToken:" + userEmail);
    }

    Long expiration = jwtTokenProvider.getExpiration(logout.getAccessToken());

    redisTemplate.opsForValue()
        .set(logout.getAccessToken(), "logout token", expiration, TimeUnit.MILLISECONDS);

  }

  public void checkEmail(Email emailRequest) {
    userRepository.findByEmail(emailRequest.getEmail())
        .ifPresent((email) -> { throw new ConflictException("?????? ???????????? ??????????????????.", ErrorCode.EMAIL_DUPLICATION);});
  }

  public UserDetailResponse getUserDetail(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("?????? ????????? ???????????? ????????????.", ErrorCode.NOT_FOUND_USER));
    return UserConverter.toUserDetail(user);
  }

  @Transactional
  public void changePassword(UUID userId, NewPassword passwordRequest) {

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("?????? ????????? ???????????? ????????????.", ErrorCode.NOT_FOUND_USER));

    if(!user.matchPassword(passwordRequest.getOldPassword())) throw new BadRequestException("?????? ??????????????? ??????????????????.", ErrorCode.WRONG_PASSWORD_INPUT);
    if(user.matchPassword(passwordRequest.getNewPassword())) throw new ConflictException("????????? ??????????????? ????????????.", ErrorCode.PASSWORD_DUPLICATION);

    user.changePassword(passwordRequest.getNewPassword());
    userRepository.save(user);
  }

  @Transactional
  public void delete(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("?????? ????????? ???????????? ????????????.", ErrorCode.NOT_FOUND_USER));

    userRepository.delete(user);
  }

  @Transactional
  public UserDetailResponse changeInfo(ChangeInfo changeInfo, UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("?????? ????????? ???????????? ????????????.", ErrorCode.NOT_FOUND_USER));

    user.changeName(changeInfo.getName());
    user.changeBoundary(changeInfo.getBoundary());
    return UserConverter.toUserDetail(userRepository.save(user));
  }

  @Transactional
  public void renewalPassword(RenewalPassword renewalPassword) {

    User user = userRepository.findByEmail(renewalPassword.getEmail())
        .orElseThrow(() -> new NotFoundException("?????? ????????? ???????????? ????????????.", ErrorCode.NOT_FOUND_USER));

    if(user.matchPassword(renewalPassword.getNewPassword())) throw new ConflictException("?????? ??????????????? ??????????????? ????????? ??? ????????????.", ErrorCode.PASSWORD_DUPLICATION);

    user.changePassword(renewalPassword.getNewPassword());
    userRepository.save(user);
  }
}
