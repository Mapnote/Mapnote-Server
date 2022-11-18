package com.mapnote.mapnoteserver.domain.schedule.controller;

import com.mapnote.mapnoteserver.domain.common.dto.DataResponse;
import com.mapnote.mapnoteserver.domain.common.dto.ErrorResponse;
import com.mapnote.mapnoteserver.domain.schedule.dto.ScheduleRequest;
import com.mapnote.mapnoteserver.domain.schedule.dto.ScheduleResponse;
import com.mapnote.mapnoteserver.domain.schedule.dto.ScheduleResponse.ScheduleDetail;
import com.mapnote.mapnoteserver.domain.schedule.dto.ScheduleResponse.ScheduleSummary;
import com.mapnote.mapnoteserver.domain.schedule.entity.ScheduleStatus;
import com.mapnote.mapnoteserver.domain.schedule.service.ScheduleService;
import com.mapnote.mapnoteserver.security.CustomUserDetails;
import com.mapnote.mapnoteserver.security.aop.Auth;
import com.mapnote.mapnoteserver.security.aop.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "schedule", description = "스케줄 API")
@RestController
@RequestMapping("/api/v1/schedule")
public class ScheduleController {

  private final ScheduleService scheduleService;

  public ScheduleController(
      ScheduleService scheduleService) {
    this.scheduleService = scheduleService;
  }

  @Operation(summary = "create schedule", description = "스케줄을 생성")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "UN AUTHORIZATION", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Auth
  @PostMapping("")
  public ResponseEntity<DataResponse<ScheduleResponse.ScheduleDetail>> createSchedule(@CurrentUser CustomUserDetails user, @RequestBody
      ScheduleRequest.Create create) {

    ScheduleDetail scheduleDetail = scheduleService.create(user.getId(), create);
    DataResponse<ScheduleResponse.ScheduleDetail> response = new DataResponse<>(ScheduleResponseCode.CREATE_SUCCESS, scheduleDetail);
    return ResponseEntity.created(URI.create("/")).body(response);
  }

  @Operation(summary = "get schedule list", description = "스케줄 리스트 조회")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "UN AUTHORIZATION", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Auth
  @GetMapping("/list/{status}")
  public ResponseEntity<DataResponse<Slice<ScheduleSummary>>> getScheduleList(@CurrentUser CustomUserDetails user,
      @PathVariable String status,
      @PageableDefault(page = 0, size = 5) Pageable pageable) {

    ScheduleStatus scheduleStatus = ScheduleStatus.valueOf(status.toUpperCase());

    Slice<ScheduleSummary> summaryList = scheduleService.findScheduleList(user.getId(), pageable, scheduleStatus);

    DataResponse<Slice<ScheduleSummary>> response = new DataResponse<>(ScheduleResponseCode.GET_SCHEDULE_LIST, summaryList);

    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @Operation(summary = "get schedule detail", description = "스케줄 정보 상세 조회")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "UN AUTHORIZATION", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Auth
  @GetMapping("/{scheduleId}")
  public ResponseEntity<DataResponse<ScheduleResponse.ScheduleDetail>> getScheduleDetail(@CurrentUser CustomUserDetails user, @PathVariable Long scheduleId) {

    ScheduleDetail scheduleDetail = scheduleService.findSchedule(user.getId(), scheduleId);

    DataResponse<ScheduleResponse.ScheduleDetail> response = new DataResponse<>(ScheduleResponseCode.CREATE_SUCCESS, scheduleDetail);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @Auth
  @GetMapping("/location")
  public ResponseEntity<DataResponse<Slice<ScheduleResponse.ScheduleSummary>>> getScheduleListByLocation (@CurrentUser CustomUserDetails user,
      @RequestBody ScheduleRequest.Location  location, @PageableDefault(page = 0, size = 5) Pageable pageable) {

    Slice<ScheduleSummary> summaryList = scheduleService.findScheduleByBoundary(user.getId(), location, pageable);
    DataResponse<Slice<ScheduleSummary>> response = new DataResponse<>(ScheduleResponseCode.GET_SCHEDULE_LIST, summaryList);

    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @Operation(summary = "toggle schedule status", description = "스케줄의 진행중/완료 상태 변경")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "UN AUTHORIZATION", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Auth
  @PutMapping("/status/{scheduleId}")
  public ResponseEntity<DataResponse<ScheduleResponse.ScheduleDetail>> toggleScheduleStatus(@CurrentUser CustomUserDetails user, @PathVariable Long scheduleId) {

    ScheduleDetail scheduleDetail = scheduleService.toggleStatus(user.getId(), scheduleId);

    DataResponse<ScheduleResponse.ScheduleDetail> response = new DataResponse<>(ScheduleResponseCode.CREATE_SUCCESS, scheduleDetail);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @Operation(summary = "update schedule info", description = "스케줄 정보 변경(내용, 장소)")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "UN AUTHORIZATION", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Auth
  @PutMapping("/{scheduleId}")
  public ResponseEntity<DataResponse<ScheduleResponse.ScheduleDetail>> updateSchedule(@CurrentUser CustomUserDetails user,
      @PathVariable Long scheduleId,
      @RequestBody ScheduleRequest.Update update) {
    ScheduleDetail scheduleDetail = scheduleService.update(user.getId(), scheduleId, update);

    DataResponse<ScheduleResponse.ScheduleDetail> response = new DataResponse<>(ScheduleResponseCode.CREATE_SUCCESS, scheduleDetail);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @Operation(summary = "delete schedule", description = "스케줄 삭제")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "UN AUTHORIZATION", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @Auth
  @DeleteMapping("/{scheduleId}")
  public ResponseEntity<DataResponse<Void>> delete(@CurrentUser CustomUserDetails user, @PathVariable Long scheduleId) {
    scheduleService.delete(user.getId() ,scheduleId);
    DataResponse<Void> response = new DataResponse<>(ScheduleResponseCode.SCHEDULE_DELETE,null);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));

  }


}
