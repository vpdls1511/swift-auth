package me.ngyu.swift.auth.domain.client.service;

import me.ngyu.swift.auth.domain.client.dto.OAuthClientDto;
import me.ngyu.swift.auth.domain.client.entity.OAuthClient;
import me.ngyu.swift.auth.domain.client.repository.OAuthClientRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OAuthClientServiceTest {

  @InjectMocks
  private OAuthClientService oAuthClientService;

  @Mock
  private OAuthClientRepository oAuthClientRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Test
  @DisplayName("클라이언트 등록 성공")
  void register_success() {
    // given
    OAuthClientDto.RegisterRequest request = new OAuthClientDto.RegisterRequest("My Project", "https://myapp.com/callback", "read,write");
    when(passwordEncoder.encode(any())).thenReturn("encodedSecret");

    // when
    OAuthClientDto.RegisterResponse response = oAuthClientService.register(request);

    // then
    verify(oAuthClientRepository, times(1)).save(any(OAuthClient.class));
    assertNotNull(response.clientId());
    assertNotNull(response.clientSecret());
  }
}
