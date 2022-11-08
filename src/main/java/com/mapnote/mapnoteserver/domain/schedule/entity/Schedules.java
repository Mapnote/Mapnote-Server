package com.mapnote.mapnoteserver.domain.schedule.entity;

import com.mapnote.mapnoteserver.domain.common.entity.BaseEntity;
import com.mapnote.mapnoteserver.domain.schedule.vo.Place;
import com.mapnote.mapnoteserver.domain.user.entity.User;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "schedules")
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE schedules SET is_deleted = true WHERE id = ?")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Schedules extends BaseEntity {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "content")
  private String content;

  @Embedded
  @Column(name = "place")
  private Place place;

  @Enumerated(EnumType.STRING)
  @Column(name = "category")
  @Builder.Default
  private Category category = Category.NONE;

  @Enumerated(EnumType.STRING)
  @Column(name = "schedule_status")
  private ScheduleStatus scheduleStatus;

  @Enumerated(EnumType.STRING)
  @Column(name = "alarm_status")
  private AlarmStatus alarmStatus;

  @Column(name = "is_deleted")
  private Boolean isDeleted = Boolean.FALSE;

}