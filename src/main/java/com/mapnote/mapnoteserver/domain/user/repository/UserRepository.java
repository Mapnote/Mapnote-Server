package com.mapnote.mapnoteserver.domain.user.repository;

import com.mapnote.mapnoteserver.domain.user.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByEmail(String userEmail);
}
