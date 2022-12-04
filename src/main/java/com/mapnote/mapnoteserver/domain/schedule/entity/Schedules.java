package com.mapnote.mapnoteserver.domain.schedule.entity;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

import com.mapnote.mapnoteserver.domain.common.entity.BaseEntity;
import com.mapnote.mapnoteserver.domain.common.exception.BadRequestException;
import com.mapnote.mapnoteserver.domain.common.exception.ErrorCode;
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
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "schedules")
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE schedules SET is_deleted = true WHERE id = ?")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Schedules extends BaseEntity {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
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
  @Builder.Default
  private Boolean isDeleted = Boolean.FALSE;

  public Schedules(User user, String content,
      Place place) {
    addUser(user);
    setContent(content);
    setPlace(place);
    this.category = Category.NONE;
    this.scheduleStatus = ScheduleStatus.ONGOING;
    this.alarmStatus = AlarmStatus.NOT_CRY;
  }

  public void addUser(User user) {
    if (isNull(user)) throw new BadRequestException("유저 정보가 잘못됐습니다.", ErrorCode.WRONG_INPUT_INVALID);
    if(this.user != null) this.user.getSchedules().remove(this);
    user.addSchedule(this);
    this.user = user;
  }

  public void toggleStatus() {
    if(this.getScheduleStatus() == ScheduleStatus.ONGOING) this.scheduleStatus = ScheduleStatus.FINISH;
    else this.scheduleStatus = ScheduleStatus.ONGOING;
  }

  public void changeContent(String content) {
    setContent(content);
  }

  public void changePlace(Place place) {
    setPlace(place);
  }

  private boolean checkBlank(String target) {
    if(isBlank(target)) throw new BadRequestException("잘못된 값이 들어왔습니다.", ErrorCode.WRONG_INPUT_INVALID);
    return true;
  }

  private void setContent(String content) {
    if(checkBlank(content)) this.content = content;
  }

  private void setPlace(Place place) {
    if(!isNull(place)) this.place = place;
  }
}
