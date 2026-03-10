package me.ngyu.swift.auth.domain.client.controller;

import lombok.RequiredArgsConstructor;
import me.ngyu.swift.auth.domain.client.dto.OAuthClientDto;
import me.ngyu.swift.auth.domain.client.service.OAuthClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class OAuthClientController {

  private final OAuthClientService oAuthClientService;

  @PostMapping("/register")
  public ResponseEntity<OAuthClientDto.RegisterResponse> register(@RequestBody OAuthClientDto.RegisterRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(oAuthClientService.register(request));
  }
}
