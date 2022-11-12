package com.mapnote.mapnoteserver.domain.alarm.controller;

import com.mapnote.mapnoteserver.domain.alarm.dto.AlarmRequest;
import com.mapnote.mapnoteserver.domain.alarm.dto.AlarmResponse;
import com.mapnote.mapnoteserver.domain.alarm.service.AlarmService;
import com.mapnote.mapnoteserver.domain.common.dto.DataResponse;
import com.mapnote.mapnoteserver.domain.common.dto.ErrorResponse;
import com.mapnote.mapnoteserver.domain.map.service.MapService;
import com.mapnote.mapnoteserver.domain.schedule.controller.ScheduleResponseCode;
import com.mapnote.mapnoteserver.domain.schedule.dto.ScheduleRequest;
import com.mapnote.mapnoteserver.domain.schedule.dto.ScheduleResponse;
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
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "alarm", description = "알람 API")
@RestController
@RequestMapping("/api/v1/alarm")
public class AlarmController {
  private final AlarmService alarmService;
  public AlarmController(AlarmService alarmService) {
    this.alarmService = alarmService;
  }

  @Operation(summary = "get fcm token", description = "firebase 토큰을 발급받습니다. ")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "UN AUTHORIZATION", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })

  @GetMapping("/token")
  public ResponseEntity<DataResponse<AlarmResponse.FireBaseToken>> getFcmToken() throws IOException {
    AlarmResponse.FireBaseToken accessToken=alarmService.getAccessToken();
    DataResponse<AlarmResponse.FireBaseToken> response = new DataResponse<>(AlarmResponseCode.GET_FIREBASE_TOKEN, accessToken);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }
  @Auth
  @PostMapping("")
  public ResponseEntity<DataResponse<Void>> sendAlarm(@CurrentUser CustomUserDetails user,
      @RequestBody AlarmRequest.CurLocation curLocation) {
    DataResponse<Void> response = new DataResponse<>(AlarmResponseCode.SEND_ALARM, null);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }
}
