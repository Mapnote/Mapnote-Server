package com.mapnote.mapnoteserver.domain.memo.service;



import com.mapnote.mapnoteserver.domain.common.exception.ErrorCode;
import com.mapnote.mapnoteserver.domain.common.exception.NotFoundException;
import com.mapnote.mapnoteserver.domain.memo.dto.MemoRequest;
import com.mapnote.mapnoteserver.domain.memo.entity.Memo;

import com.mapnote.mapnoteserver.domain.memo.repository.MemoRepository;
import com.mapnote.mapnoteserver.domain.user.entity.User;
import com.mapnote.mapnoteserver.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemoService {
  private final UserRepository userRepository;
  private final MemoRepository memoRepository;

  @Transactional
  public Long memoSave(MemoRequest.Save memoSave, UUID uuid) {
    User user=userRepository.findById(uuid).orElse(null);
    Memo memo=Memo.builder()
            .user(user)
            .contents(memoSave.getContents())
            .type(memoSave.getType())
            .build();
    memoRepository.save(memo);
    return memo.getId();
  }
  @Transactional
  public Long memoDelete(MemoRequest.Delete memoDelete) {
    Memo memo = memoRepository.findById(memoDelete.getId())
            .orElseThrow(() -> new NotFoundException("해당 유저가 존재하지 않습니다.", ErrorCode.NOT_FOUND_MEMO));

    memoRepository.delete(memo);
    return memo.getId();
  }

}
