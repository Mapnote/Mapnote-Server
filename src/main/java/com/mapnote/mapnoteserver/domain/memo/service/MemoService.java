package com.mapnote.mapnoteserver.domain.memo.service;



import com.mapnote.mapnoteserver.domain.memo.dto.MemoRequest;
import com.mapnote.mapnoteserver.domain.memo.entity.Memo;

import com.mapnote.mapnoteserver.domain.memo.repository.MemoRepository;
import com.mapnote.mapnoteserver.domain.user.entity.Authority;
import com.mapnote.mapnoteserver.domain.user.entity.User;
import com.mapnote.mapnoteserver.domain.user.repository.UserRepository;
import com.mapnote.mapnoteserver.domain.user.util.PasswordEncrypter;
import com.mapnote.mapnoteserver.domain.user.util.UserConverter;
import com.mapnote.mapnoteserver.security.jwt.JwtExpiration;
import com.mapnote.mapnoteserver.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemoService {
  private final MemoRepository memoRepository;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final JwtTokenProvider jwtTokenProvider;
  private final RedisTemplate redisTemplate;

  private static final String BEARER_TYPE = "Bearer";

  @Transactional
  public Long memoSave(MemoRequest.Save memoSave) {
    Memo memo=Memo.builder()
            .contents(memoSave.getContents())
            .type(memoSave.getType())
            .build();
    memoRepository.save(memo);
    return memo.getId();
  }



}
