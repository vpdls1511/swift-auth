package me.ngyu.swift.auth.domain.user.controller;

import lombok.RequiredArgsConstructor;
import me.ngyu.swift.auth.domain.user.dto.UserDto;
import me.ngyu.swift.auth.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
