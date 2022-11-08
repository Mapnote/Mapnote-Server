package com.mapnote.mapnoteserver.domain.schedule.util;

import com.mapnote.mapnoteserver.domain.schedule.dto.ScheduleRequest;
import com.mapnote.mapnoteserver.domain.schedule.dto.ScheduleResponse;
import com.mapnote.mapnoteserver.domain.schedule.entity.AlarmStatus;
import com.mapnote.mapnoteserver.domain.schedule.entity.Schedules;
import com.mapnote.mapnoteserver.domain.schedule.entity.ScheduleStatus;
import com.mapnote.mapnoteserver.domain.schedule.vo.Address;
import com.mapnote.mapnoteserver.domain.schedule.vo.Place;
import com.mapnote.mapnoteserver.domain.user.entity.User;

public class ScheduleConverter {

  public static Schedules toSchedule(ScheduleRequest.Create create, User user) {
    return Schedules.builder()
        .user(user)
        .content(create.getContent())
        .place(Place.builder()
            .name(create.getPlaceName())
            .latitude(create.getLatitude())
            .longitude(create.getLongitude())
            .address(new Address(create.getRoadAddress(), create.getAddress()))
            .build())
        .scheduleStatus(ScheduleStatus.ONGOING)
        .alarmStatus(AlarmStatus.NOT_CRY)
        .build();
  }

  public static ScheduleResponse.ScheduleDetail toDetail(Schedules schedule) {
    return ScheduleResponse.ScheduleDetail.builder()
        .content(schedule.getContent())
        .category(schedule.getCategory())
        .place(schedule.getPlace())
        .scheduleStatus(schedule.getScheduleStatus())
        .createdAt(schedule.getCreatedAt())
        .updatedAt(schedule.getUpdatedAt())
        .build();
  }

}
