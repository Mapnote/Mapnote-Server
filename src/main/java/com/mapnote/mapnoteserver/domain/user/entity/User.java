package com.mapnote.mapnoteserver.domain.user.entity;

import static java.util.Objects.isNull;
import static org.apache.logging.log4j.util.Strings.isBlank;

import com.mapnote.mapnoteserver.domain.common.entity.BaseEntity;
import com.mapnote.mapnoteserver.domain.common.exception.BadRequestException;
import com.mapnote.mapnoteserver.domain.common.exception.ErrorCode;
import com.mapnote.mapnoteserver.domain.schedule.entity.Schedules;
import com.mapnote.mapnoteserver.domain.user.util.PasswordEncrypter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "users")
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id = ?")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {

  private static final int[] boundaryList = {2, 4, 6, 8 , 10};

  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(name = "id", columnDefinition = "BINARY(16)")
  private UUID id;

  @Column(name = "email")
  private String email;

  @Column(name = "password")
  private String password;

  @Column(name = "name")
  private String name;

  @Column(name = "boundary")
  @Builder.Default
  private Long boundary = 6L;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Schedules> schedules = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private Set<Authority> authorities = new HashSet<>();

  @Column(name = "is_deleted")
  @Builder.Default
  private Boolean isDeleted = Boolean.FALSE;

  public List<String> getAuthorities() {
    return authorities.stream()
        .map(Authority::getRole)
        .collect(Collectors.toList());
  }

  public void addAuthority(Authority authority) {
    authorities.add(authority);
  }

  public boolean matchPassword(String password) {
    return PasswordEncrypter.isMatch(password, this.password);
  }

  public void changePassword(String password) {
    setPassword(PasswordEncrypter.encrypt(password));
  }

  public void changeName(String name) {
    setName(name);
  }

  public void changeBoundary(Long boundary) {
    setBoundary(boundary);
  }

  private void setPassword(String password) {
    if(isBlank(password)) throw new BadRequestException("잘못된 형식의 패스워드가 입력됬습니다.", ErrorCode.WRONG_PASSWORD_INPUT);
    this.password = password;
  }

  private void setName(String name) {
    if(isBlank(name)) throw new BadRequestException("잘못된 형식의 이름이 입력되었습니다.", ErrorCode.WRONG_INPUT_INVALID);
    this.name = name;
  }

  private void setBoundary(Long boundary) {
    if(isNull(boundary)) throw new BadRequestException("NULL 값은 설정할 수 없습니다.", ErrorCode.WRONG_INPUT_INVALID);
    if(Arrays.stream(boundaryList).noneMatch(b -> boundary == b)) throw new BadRequestException("잘못된 값의 반경이 입력되었습니다.", ErrorCode.WRONG_INPUT_INVALID);
    this.boundary = boundary;
  }
}
