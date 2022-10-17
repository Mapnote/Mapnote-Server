package com.mapnote.mapnoteserver.security.jwt;

import com.mapnote.mapnoteserver.domain.common.exception.BadRequestException;
import com.mapnote.mapnoteserver.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.yml")
public class JwtTokenProvider {

  private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);
  private static final String AUTHORITIES_KEY = "authKey";

  @Value("${jwt.secret}")
  private String secretKey;

  @PostConstruct
  protected void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  public String generateAccessToken(String username) {
    return generateToken(username, JwtExpiration.ACCESS_TOKEN_EXPIRATION_TIME.getExpirationTime());
  }

  public String generateRefreshToken(String username) {
    return generateToken(username, JwtExpiration.REFRESH_TOKEN_EXPIRATION_TIME.getExpirationTime());
  }

  public Boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
      return !isTokenExpired(token);
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      log.info("Invalid JWT Token", e);
    } catch (ExpiredJwtException e) {
      log.info("Expired JWT Token", e);
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT Token", e);
    } catch (IllegalArgumentException e) {
      log.info("JWT claims string is empty.", e);
    }
    return false;
  }

  public String getUserEmail(String token) {
    return extractAllClaims(token).get(AUTHORITIES_KEY, String.class);
  }

  public Authentication getAuthentication(String accessToken) {
    // 토큰 복호화
    Claims claims = extractAllClaims(accessToken);

    if(claims.get(AUTHORITIES_KEY) == null) throw new BadRequestException("권한 정보가 없는 토큰입니다.");

    // 클레임에서 권한 정보 가져오기
    Collection<? extends GrantedAuthority> authorities =
        Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

    UserDetails principal = new User(claims.getSubject(), "", authorities);
    return new UsernamePasswordAuthenticationToken(principal, "", authorities);
  }

  public Long getExpiration(String token) {
    Date expiration = extractAllClaims(token).getExpiration();

    Long now = new Date().getTime();
    return (expiration.getTime() - now);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey(secretKey))
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private Key getSigningKey(String secretKey) {
    byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private Boolean isTokenExpired(String token) {
    Date expiration = extractAllClaims(token).getExpiration();
    return expiration.before(new Date());
  }

  private String generateToken(String username, Long expireTime) {
    Claims claims = Jwts.claims();
    claims.put(AUTHORITIES_KEY, username);
    Date now = new Date(System.currentTimeMillis());

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + expireTime))
        .signWith(getSigningKey(secretKey), SignatureAlgorithm.HS256)
        .compact();
  }

}
