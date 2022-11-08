package com.mapnote.mapnoteserver.domain.schedule.service;

import com.mapnote.mapnoteserver.domain.common.exception.ErrorCode;
import com.mapnote.mapnoteserver.domain.common.exception.NotFoundException;
import com.mapnote.mapnoteserver.domain.schedule.dto.ScheduleRequest.Create;
import com.mapnote.mapnoteserver.domain.schedule.dto.ScheduleResponse.ScheduleDetail;
import com.mapnote.mapnoteserver.domain.schedule.entity.Schedules;
import com.mapnote.mapnoteserver.domain.schedule.repository.ScheduleRepository;
import com.mapnote.mapnoteserver.domain.schedule.util.ScheduleConverter;
import com.mapnote.mapnoteserver.domain.user.entity.User;
import com.mapnote.mapnoteserver.domain.user.repository.UserRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ScheduleService {

  private final ScheduleRepository scheduleRepository;
  private final UserRepository userRepository;

  public ScheduleService(
      ScheduleRepository scheduleRepository,
      UserRepository userRepository) {
    this.scheduleRepository = scheduleRepository;
    this.userRepository = userRepository;
  }


  public ScheduleDetail create(UUID id, Create create) {

    User user = userRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("유저가 존재하지 않습니다.", ErrorCode.NOT_FOUND_USER));

    Schedules newSchedule = scheduleRepository.save(ScheduleConverter.toSchedule(create, user));

    return ScheduleConverter.toDetail(newSchedule);
  }
}
