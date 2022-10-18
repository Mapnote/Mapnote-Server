package com.mapnote.mapnoteserver.domain.user.controller;

import com.mapnote.mapnoteserver.domain.user.dto.UserRequest;
import com.mapnote.mapnoteserver.domain.user.dto.UserResponse;
import com.mapnote.mapnoteserver.domain.user.dto.UserResponse.TokenResponse;
import com.mapnote.mapnoteserver.domain.user.service.UserService;
import java.net.URI;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("")
  public ResponseEntity<UUID> signUp(@Validated @RequestBody UserRequest.SignUp signUpRequest) {
    UUID id = userService.signUp(signUpRequest);
    return ResponseEntity.created(URI.create("/")).body(id);
  }

  @PostMapping("/login")
  public ResponseEntity<UserResponse.TokenResponse> login(@Validated @RequestBody UserRequest.Login loginRequest) {
    TokenResponse loginResponse = userService.login(loginRequest);
    return ResponseEntity.ok(loginResponse);
  }

  @PostMapping("/reissue")
  public ResponseEntity<UserResponse.TokenResponse> reissue(@Validated @RequestBody UserRequest.Reissue reissueRequest) {
    TokenResponse reissueResponse = userService.reissue(reissueRequest);
    return ResponseEntity.ok(reissueResponse);
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(@Validated @RequestBody UserRequest.Logout logoutRequest) {
    userService.logout(logoutRequest);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/email")
  public ResponseEntity<Void> emailCheck(@Validated @RequestBody UserRequest.Email emailRequest) {
    userService.checkEmail(emailRequest);

    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserResponse.UserDetailResponse> getUserDetail(@PathVariable UUID userId) {
    UserResponse.UserDetailResponse userDetail = userService.getUserDetail(userId);
    return ResponseEntity.ok(userDetail);
  }

}
