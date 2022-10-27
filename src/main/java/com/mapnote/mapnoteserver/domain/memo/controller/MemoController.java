package com.mapnote.mapnoteserver.domain.memo.controller;

import com.mapnote.mapnoteserver.domain.common.dto.DataResponse;
import com.mapnote.mapnoteserver.domain.memo.dto.MemoRequest;
import com.mapnote.mapnoteserver.domain.memo.dto.MemoResponse;
import com.mapnote.mapnoteserver.domain.memo.service.MemoService;
import com.mapnote.mapnoteserver.domain.user.controller.UserResponseCode;
import com.mapnote.mapnoteserver.domain.user.dto.UserRequest;
import com.mapnote.mapnoteserver.domain.user.dto.UserResponse;
import com.mapnote.mapnoteserver.domain.user.dto.UserResponse.TokenResponse;
import com.mapnote.mapnoteserver.domain.user.dto.UserResponse.UserSignupResponse;
import com.mapnote.mapnoteserver.domain.user.entity.User;
import com.mapnote.mapnoteserver.security.CustomUserDetails;
import com.mapnote.mapnoteserver.security.aop.Auth;
import com.mapnote.mapnoteserver.security.aop.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/memos")
public class MemoController {

  private final MemoService memoService;

  @PostMapping("")
  public ResponseEntity<DataResponse<MemoResponse.MemoSaveResponse>> memoSave(@CurrentUser CustomUserDetails user, @Validated @RequestBody MemoRequest.Save saveRequest) {
    Long id = memoService.memoSave(saveRequest, user.getId());
    DataResponse<MemoResponse.MemoSaveResponse> response = new DataResponse<>(MemoResponseCode.SAVE_SUCCESS, new MemoResponse.MemoSaveResponse(id));
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }
  /*
  @GetMapping("")
  public ResponseEntity<DataResponse<MemoResponse.MemoSaveResponse>> memoLoad(@CurrentUser UUID uuid) {
    //Long id = memoService.memoSave(saveRequest, uuid);
    DataResponse<MemoResponse.MemoSaveResponse> response = new DataResponse<>(MemoResponseCode.SAVE_SUCCESS, new MemoResponse.MemoSaveResponse(id));
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

  @PatchMapping("")
  public ResponseEntity<DataResponse<MemoResponse.MemoSaveResponse>> memoUpdate(@Validated @RequestBody MemoRequest.Save saveRequest) {
    Long id = memoService.memoSave(saveRequest, uuid);
    DataResponse<MemoResponse.MemoSaveResponse> response = new DataResponse<>(MemoResponseCode.SAVE_SUCCESS, new MemoResponse.MemoSaveResponse(id));
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }
    */
  @DeleteMapping("")
  public ResponseEntity<DataResponse<Void>> memoDelete(@Validated @RequestBody MemoRequest.Delete deleteRequest) {
    memoService.memoDelete(deleteRequest);
    DataResponse<Void> response = new DataResponse<>(MemoResponseCode.DELETE_SUCCESS,null);
    return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
  }

}
