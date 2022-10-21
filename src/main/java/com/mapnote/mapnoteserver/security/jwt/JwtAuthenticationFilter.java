package com.mapnote.mapnoteserver.security.jwt;

import static org.springframework.util.ObjectUtils.isEmpty;

import com.mapnote.mapnoteserver.security.CustomUserDetailService;
import com.mapnote.mapnoteserver.security.CustomUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
  private final JwtTokenProvider jwtTokenProvider;
  private final CustomUserDetailService customUserDetailService;
  private final RedisTemplate redisTemplate;

  public JwtAuthenticationFilter(
      JwtTokenProvider jwtTokenProvider,
      CustomUserDetailService customUserDetailService,
      RedisTemplate redisTemplate) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.customUserDetailService = customUserDetailService;
    this.redisTemplate = redisTemplate;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    try {
      String accessToken = getAccessToken(request);
      if(accessToken != null) {
        String userEmail = jwtTokenProvider.getUserEmail(accessToken);
        String isLogout = (String) redisTemplate.opsForValue().get(accessToken);
        // 토큰이 유효함
        if(isEmpty(isLogout)) {
          CustomUserDetails userDetails = customUserDetailService.loadUserByUsername(userEmail);
          validateAccessToken(accessToken);
          setSecurityContext(request, userDetails);
        }
      }
    } catch (SignatureException | MalformedJwtException e) {
      logger.info("토큰이 변조되었습니다.");
    } catch (ExpiredJwtException e) {
      logger.info("토큰이 만료되었습니다.");
    }
    filterChain.doFilter(request, response);
  }

  private String getAccessToken(HttpServletRequest request) {
    String header = request.getHeader("Authorization");
    if(StringUtils.hasText(header)) return header;
    return null;
  }

  private void validateAccessToken(String accessToken) {
    if(!jwtTokenProvider.validateToken(accessToken)) {
      throw new IllegalArgumentException("토큰 검증에 실패했습니다.");
    }
  }

  private void setSecurityContext(HttpServletRequest request, UserDetails userDetails) {
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
        userDetails, null, userDetails.getAuthorities());

    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
  }

}
