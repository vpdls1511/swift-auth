package me.ngyu.swift.auth.domain.user.controller;

import lombok.RequiredArgsConstructor;
import me.ngyu.swift.auth.domain.user.dto.TokenResponse;
import me.ngyu.swift.auth.domain.user.dto.UserDto;
import me.ngyu.swift.auth.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<Void> register(@RequestBody UserDto.UserRegisterRequest request) {
    userService.register(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/login")
  public ResponseEntity<TokenResponse> login(@RequestBody UserDto.UserLoginRequest request) {
    return ResponseEntity.ok(userService.login(request));
  }

  @GetMapping("/me")
  public ResponseEntity<UserDto.UserResponse> getMyInfo(Authentication authentication) {
    Long userId = (Long) authentication.getPrincipal();
    return ResponseEntity.ok(userService.getMyInfo(userId));
  }

  @PostMapping("/refresh")
  public ResponseEntity<TokenResponse> refresh(@RequestBody Map<String, String> body) {
    return ResponseEntity.ok(userService.refresh(body.get("refreshToken")));
  }

}
