package com.mapnote.mapnoteserver.domain.user.service;


import static org.springframework.util.ObjectUtils.isEmpty;

import com.mapnote.mapnoteserver.domain.common.exception.BadRequestException;
import com.mapnote.mapnoteserver.domain.common.exception.ConflictException;
import com.mapnote.mapnoteserver.domain.common.exception.NotFoundException;
import com.mapnote.mapnoteserver.domain.user.dto.UserRequest;
import com.mapnote.mapnoteserver.domain.user.dto.UserRequest.Email;
import com.mapnote.mapnoteserver.domain.user.dto.UserResponse;
import com.mapnote.mapnoteserver.domain.user.entity.Authority;
import com.mapnote.mapnoteserver.domain.user.entity.User;
import com.mapnote.mapnoteserver.domain.user.repository.UserRepository;
import com.mapnote.mapnoteserver.security.jwt.JwtExpiration;
import com.mapnote.mapnoteserver.security.jwt.JwtTokenProvider;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
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

  public UUID signUp(UserRequest.SignUp signUp) {

    User user = User.builder()
        .email(signUp.getEmail())
        .password(signUp.getPassword())
        .name(signUp.getName())
        .build();

    Authority.addAdminAuth(user);

    userRepository.save(user);

    return user.getId();
  }

  public UserResponse.TokenResponse login(UserRequest.Login login) {
    userRepository.findByEmail(login.getEmail())
        .orElseThrow(() -> new NotFoundException("해당 유저는 존재하지 않습니다."));

    // Login id, pw 기반 Authentication 객체 생성
    UsernamePasswordAuthenticationToken authenticationToken = login.toAuthentication();

    // 검증
    Authentication authentication = authenticationManagerBuilder.getObject()
        .authenticate(authenticationToken);

    // 인증 정보 기반 JWT 토큰 생성
    String accessToken = jwtTokenProvider.generateAccessToken(authentication.getName());
    String refreshToken = jwtTokenProvider.generateRefreshToken(authentication.getName());

    // refreshToken redis 에 저장
    redisTemplate.opsForValue()
        .set("RefreshToken:" + authentication.getName(), refreshToken);

    return UserResponse.TokenResponse.builder()
        .grantType(BEARER_TYPE)
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .refreshTokenExpirationTime(JwtExpiration.REFRESH_TOKEN_EXPIRATION_TIME.getExpirationTime())
        .build();
  }

  public UserResponse.TokenResponse reissue(UserRequest.Reissue reissue) {

    // refresh token 검증 (유효기간)
    if(!jwtTokenProvider.validateToken(reissue.getRefreshToken())) throw new BadRequestException("Refresh Token 정보가 유효하지 않습니다.");

    // access Token 에서 email 추출
    String userEmail = jwtTokenProvider.getUserEmail(reissue.getAccessToken());
//    Authentication authentication = jwtTokenProvider.getAuthentication(reissue.getAccessToken());

    // Redis 에서 email로 저장된 refresh token 값 가져오기
    String refreshToken = (String) redisTemplate.opsForValue().get("RefreshToken:" + userEmail);

    // 로그아웃 된 토큰인지 확인
    if(isEmpty(refreshToken)) throw new BadRequestException("Refresh Token 정보가 유효하지 않습니다.");
    if(!refreshToken.equals(reissue.getRefreshToken())) throw new BadRequestException("Refresh Token 정보가 일치하지 않습니다.");

    // 토큰 새로 생성
    String newAccessToken = jwtTokenProvider.generateAccessToken(userEmail);
    String newRefreshToken = jwtTokenProvider.generateRefreshToken(userEmail);

    // 업데이트
    redisTemplate.opsForValue()
        .set("RefreshToken:" + userEmail, newRefreshToken);

    return UserResponse.TokenResponse.builder()
        .grantType(BEARER_TYPE)
        .accessToken(newAccessToken)
        .refreshToken(newRefreshToken)
        .refreshTokenExpirationTime(JwtExpiration.REFRESH_TOKEN_EXPIRATION_TIME.getExpirationTime())
        .build();
  }

  public void logout(UserRequest.Logout logout) {

    // token 검증
    if(!jwtTokenProvider.validateToken(logout.getAccessToken())) throw new BadRequestException("Access Token 정보가 잘못되었습니다.");

    // token 에서 email 추출
    String userEmail = jwtTokenProvider.getUserEmail(logout.getAccessToken());

    // redis 데이터 삭제
    if(redisTemplate.opsForValue().get("RefreshToken:" + userEmail) != null) {
      redisTemplate.delete("RefreshToken:" + userEmail);
    }

    Long expiration = jwtTokenProvider.getExpiration(logout.getAccessToken());

    redisTemplate.opsForValue()
        .set(logout.getAccessToken(), "logout token", expiration, TimeUnit.MILLISECONDS);

  }

  public void checkEmail(Email emailRequest) {
    userRepository.findByEmail(emailRequest.getEmail())
        .ifPresent((email) -> { throw new ConflictException("이미 존재하는 이메일입니다.");});
  }

}
