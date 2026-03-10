package me.ngyu.swift.auth.domain.client.service;

import lombok.RequiredArgsConstructor;
import me.ngyu.swift.auth.domain.client.dto.OAuthClientDto;
import me.ngyu.swift.auth.domain.client.entity.OAuthClient;
import me.ngyu.swift.auth.domain.client.repository.OAuthClientRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuthClientService {

  private final OAuthClientRepository oAuthClientRepository;
  private final PasswordEncoder passwordEncoder;

  public OAuthClientDto.RegisterResponse register(OAuthClientDto.RegisterRequest request) {
    String clientId = UUID.randomUUID().toString();
    String rawSecret = UUID.randomUUID().toString();
    String encodedSecret = passwordEncoder.encode(rawSecret);

    OAuthClient client = OAuthClient.builder()
      .clientId(clientId)
      .clientSecret(encodedSecret)
      .name(request.name())
      .redirectUri(request.redirectUri())
      .scopes(request.scopes())
      .build();

    oAuthClientRepository.save(client);

    return new OAuthClientDto.RegisterResponse(clientId, rawSecret);
  }
}
