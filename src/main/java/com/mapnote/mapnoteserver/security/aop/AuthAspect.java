package com.mapnote.mapnoteserver.security.aop;

import com.mapnote.mapnoteserver.domain.user.entity.User;
import com.mapnote.mapnoteserver.domain.user.repository.UserRepository;
import com.mapnote.mapnoteserver.security.jwt.JwtTokenProvider;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Aspect
@Component
public class AuthAspect {

  private static final String HEADER = "Authorization";

  private final JwtTokenProvider jwtTokenProvider;
  private final UserRepository userRepository;
  private final HttpServletRequest servletRequest;

  public AuthAspect(JwtTokenProvider jwtTokenProvider,
      UserRepository userRepository, HttpServletRequest servletRequest) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.userRepository = userRepository;
    this.servletRequest = servletRequest;
  }

  @Around("@annotation(com.mapnote.mapnoteserver.security.aop.Auth)")
  public Object validateToken(final ProceedingJoinPoint pjp) throws Throwable {
    String token = servletRequest.getHeader(HEADER);

    String userEmail = jwtTokenProvider.getUserEmail(token);
    User user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new HttpClientErrorException(HttpStatus.UNAUTHORIZED));
    return pjp.proceed();
  }

}
