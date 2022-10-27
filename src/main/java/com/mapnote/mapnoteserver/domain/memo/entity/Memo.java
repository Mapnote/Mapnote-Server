package com.mapnote.mapnoteserver.domain.memo.entity;

import com.mapnote.mapnoteserver.domain.common.entity.BaseEntity;
import com.mapnote.mapnoteserver.domain.memo.dto.MemoType;
import com.mapnote.mapnoteserver.domain.user.entity.User;

import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "memo")
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE memo SET is_deleted = true WHERE id = ?")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Memo extends BaseEntity {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;
  private String contents;
  private MemoType type;
  @Column(name = "is_deleted")
  private Boolean isDeleted = Boolean.FALSE;
  @Column(name = "is_checked")
  private Boolean isChecked = Boolean.FALSE;
  @Column(name = "is_alarm")
  private Boolean isAlarm = Boolean.FALSE;

}
