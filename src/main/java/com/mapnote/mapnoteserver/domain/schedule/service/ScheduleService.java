package com.mapnote.mapnoteserver.domain.schedule.service;

import com.mapnote.mapnoteserver.domain.common.exception.ErrorCode;
import com.mapnote.mapnoteserver.domain.common.exception.NotFoundException;
import com.mapnote.mapnoteserver.domain.schedule.dto.ScheduleRequest.Create;
import com.mapnote.mapnoteserver.domain.schedule.dto.ScheduleRequest.Update;
import com.mapnote.mapnoteserver.domain.schedule.dto.ScheduleResponse.ScheduleDetail;
import com.mapnote.mapnoteserver.domain.schedule.dto.ScheduleResponse.ScheduleSummary;
import com.mapnote.mapnoteserver.domain.schedule.entity.ScheduleStatus;
import com.mapnote.mapnoteserver.domain.schedule.entity.Schedules;
import com.mapnote.mapnoteserver.domain.schedule.repository.ScheduleRepository;
import com.mapnote.mapnoteserver.domain.schedule.util.ScheduleConverter;
import com.mapnote.mapnoteserver.domain.schedule.vo.Address;
import com.mapnote.mapnoteserver.domain.schedule.vo.Place;
import com.mapnote.mapnoteserver.domain.user.entity.User;
import com.mapnote.mapnoteserver.domain.user.repository.UserRepository;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

  @Transactional
  public ScheduleDetail create(UUID userId, Create create) {

    User user = isExistUser(userId);

    Schedules schedule = scheduleRepository.save(ScheduleConverter.toSchedule(create, user));

    return ScheduleConverter.toDetail(schedule);
  }

  public Slice<ScheduleSummary> findScheduleList(UUID userId, Pageable pageable, ScheduleStatus scheduleStatus) {
    Slice<Schedules> schedulesList = scheduleRepository.findByUser_IdAndScheduleStatusOrderByCreatedAtDesc(userId, scheduleStatus, pageable);

    return schedulesList.map(ScheduleConverter::toSummary);
  }

  public ScheduleDetail findSchedule(UUID userId, Long scheduleId) {
    isExistUser(userId);

    Schedules schedule = scheduleRepository.findByIdAndUser_Id(scheduleId, userId)
        .orElseThrow(
            () -> new NotFoundException("해당 스케줄이 존재하지 않습니다.", ErrorCode.NOT_FOUND_SCHEDULE));

    return ScheduleConverter.toDetail(schedule);
  }

  @Transactional
  public ScheduleDetail toggleStatus(UUID userId, Long scheduleId) {

    isExistUser(userId);

    scheduleRepository.findByIdAndUser_Id(scheduleId, userId)
        .ifPresent(Schedules::toggleStatus);

    Schedules schedule = scheduleRepository.findById(scheduleId)
        .orElseThrow(
            () -> new NotFoundException("해당 스케줄이 존재하지 않습니다.", ErrorCode.NOT_FOUND_SCHEDULE));

    return ScheduleConverter.toDetail(schedule);
  }


  @Transactional
  public ScheduleDetail update(UUID userId, Long scheduleId, Update update) {

    isExistUser(userId);

    Schedules schedule = scheduleRepository.findByIdAndUser_Id(scheduleId, userId)
        .orElseThrow(
            () -> new NotFoundException("해당 스케줄이 존재하지 않습니다.", ErrorCode.NOT_FOUND_SCHEDULE));

    schedule.changeContent(update.getContent());
    Place place = Place.builder()
        .name(update.getPlaceName())
        .longitude(update.getLongitude())
        .latitude(update.getLatitude())
        .address(new Address(update.getRoadAddress(), update.getAddress()))
        .build();
    schedule.changePlace(place);

    Schedules updateSchedule = scheduleRepository.save(schedule);

    return ScheduleConverter.toDetail(updateSchedule);
  }

  @Transactional
  public void delete(UUID userId, Long scheduleId) {
    Schedules schedule = scheduleRepository.findByIdAndUser_Id(scheduleId, userId)
        .orElseThrow(
            () -> new NotFoundException("해당 스케줄이 존재하지 않습니다.", ErrorCode.NOT_FOUND_SCHEDULE));

    scheduleRepository.delete(schedule);
  }

  private User isExistUser(UUID userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("유저가 존재하지 않습니다.", ErrorCode.NOT_FOUND_USER));
  }
}
