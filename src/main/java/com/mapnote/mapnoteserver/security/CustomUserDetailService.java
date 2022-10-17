package com.mapnote.mapnoteserver.security;

import com.mapnote.mapnoteserver.domain.common.exception.NotFoundException;
import com.mapnote.mapnoteserver.domain.user.entity.User;
import com.mapnote.mapnoteserver.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

  private final UserRepository userRepository;

  public CustomUserDetailService(
      UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
    // TODO : 예외처리 다른 것으로 변경
    return userRepository.findByEmail(userEmail)
        .map(CustomUserDetails::of)
        .orElseThrow(() -> new NotFoundException("해당 유저를 찾을 수 없습니다."));
  }
}
