package com.mapnote.mapnoteserver.security;

import com.mapnote.mapnoteserver.domain.user.entity.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomUserDetails implements UserDetails {

  private String email;

  @Builder.Default
  private List<String> authorities = new ArrayList<>();

  public CustomUserDetails(String email, List<String> authorities) {
    this.email = email;
    this.authorities = authorities;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities.stream()
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    return false;
  }

  public static CustomUserDetails of(User user) {
    return CustomUserDetails.builder()
        .email(user.getEmail())
        .authorities(user.getAuthorities())
        .build();
  }

}
