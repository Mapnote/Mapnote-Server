package com.mapnote.mapnoteserver.domain.schedule.controller;

import com.mapnote.mapnoteserver.domain.common.dto.DataResponse;
import com.mapnote.mapnoteserver.domain.schedule.dto.ScheduleRequest;
import com.mapnote.mapnoteserver.domain.schedule.dto.ScheduleResponse;
import com.mapnote.mapnoteserver.domain.schedule.dto.ScheduleResponse.ScheduleDetail;
import com.mapnote.mapnoteserver.domain.schedule.dto.ScheduleResponse.ScheduleSummary;
import com.mapnote.mapnoteserver.domain.schedule.entity.ScheduleStatus;
import com.mapnote.mapnoteserver.domain.schedule.service.ScheduleService;
import com.mapnote.mapnoteserver.security.CustomUserDetails;
import com.mapnote.mapnoteserver.security.aop.Auth;
import com.mapnote.mapnoteserver.security.aop.CurrentUser;
import java.net.URI;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  @Auth
  @PostMapping("")
  public ResponseEntity<DataResponse<ScheduleResponse.ScheduleDetail>> createSchedule(@CurrentUser CustomUserDetails user, @RequestBody
      ScheduleRequest.Create create) {

    ScheduleDetail scheduleDetail = scheduleService.create(user.getId(), create);
    DataResponse<ScheduleResponse.ScheduleDetail> response = new DataResponse<>(ScheduleResponseCode.CREATE_SUCCESS, scheduleDetail);
    return ResponseEntity.created(URI.create("/")).body(response);
  }

  @Auth
  @GetMapping("/{status}")
  public ResponseEntity<DataResponse<Slice<ScheduleSummary>>> getScheduleList(@CurrentUser CustomUserDetails user,
      @PathVariable String status,
      @PageableDefault(page = 0, size = 5) Pageable pageable) {

    ScheduleStatus scheduleStatus = ScheduleStatus.valueOf(status.toUpperCase());

    Slice<ScheduleSummary> summaryList = scheduleService.findScheduleList(user.getId(), pageable, scheduleStatus);

    DataResponse<Slice<ScheduleSummary>> response = new DataResponse<>(ScheduleResponseCode.GET_SCHEDULE_LIST, summaryList);

    return ResponseEntity.ok(response);
  }

}
