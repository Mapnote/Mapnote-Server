package com.mapnote.mapnoteserver.domain.schedule.controller;

import com.mapnote.mapnoteserver.domain.common.dto.DataResponse;
import com.mapnote.mapnoteserver.domain.schedule.dto.ScheduleRequest;
import com.mapnote.mapnoteserver.domain.schedule.dto.ScheduleResponse;
import com.mapnote.mapnoteserver.domain.schedule.dto.ScheduleResponse.ScheduleDetail;
import com.mapnote.mapnoteserver.domain.schedule.service.ScheduleService;
import com.mapnote.mapnoteserver.domain.user.controller.UserResponseCode;
import com.mapnote.mapnoteserver.domain.user.dto.UserResponse;
import com.mapnote.mapnoteserver.domain.user.dto.UserResponse.UserSignupResponse;
import com.mapnote.mapnoteserver.security.CustomUserDetails;
import com.mapnote.mapnoteserver.security.aop.CurrentUser;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/schedule")
public class ScheduleController {

  private final ScheduleService scheduleService;

  public ScheduleController(
      ScheduleService scheduleService) {
    this.scheduleService = scheduleService;
  }

  @PostMapping("")
  public ResponseEntity<DataResponse<ScheduleResponse.ScheduleDetail>> createSchedule(@CurrentUser CustomUserDetails user, @RequestBody
      ScheduleRequest.Create create) {

    ScheduleDetail scheduleDetail = scheduleService.create(user.getId(), create);
    DataResponse<ScheduleResponse.ScheduleDetail> response = new DataResponse<>(ScheduleResponseCode.CREATE_SUCCESS, scheduleDetail);
    return ResponseEntity.created(URI.create("/")).body(response);
  }


}
