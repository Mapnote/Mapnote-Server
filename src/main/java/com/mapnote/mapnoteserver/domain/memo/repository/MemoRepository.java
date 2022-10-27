package com.mapnote.mapnoteserver.domain.memo.repository;

import com.mapnote.mapnoteserver.domain.memo.entity.Memo;
import com.mapnote.mapnoteserver.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemoRepository extends JpaRepository<Memo, Long> {
  Optional<User> findByUser(User user);
}
