package me.ngyu.swift.auth.global.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtProviderTest {

  private JwtProvider jwtProvider;

  @BeforeEach
  void setUp() {
    jwtProvider = new JwtProvider();
    ReflectionTestUtils.setField(jwtProvider, "secret", "sw1ft-4uth-s3cur3-jwt-s3cr3t-k3y-2026!");
    ReflectionTestUtils.setField(jwtProvider, "accessTokenExpiration", 3600L);
    ReflectionTestUtils.setField(jwtProvider, "refreshTokenExpiration", 1209600L);
  }

  @Test
  @DisplayName("액세스 토큰 생성 성공")
  void generateAccessToken_success() {
    String token = jwtProvider.generateAccessToken(1L, "test@email.com");
    assertNotNull(token);
  }

  @Test
  @DisplayName("토큰에서 userId 추출 성공")
  void extractUserId_success() {
    String token = jwtProvider.generateAccessToken(1L, "test@email.com");
    Long userId = jwtProvider.extractUserId(token);
    assertEquals(1L, userId);
  }

  @Test
  @DisplayName("토큰 유효성 검증 성공")
  void validateToken_success() {
    String token = jwtProvider.generateAccessToken(1L, "test@email.com");
    assertTrue(jwtProvider.validateToken(token));
  }

  @Test
  @DisplayName("만료된 토큰 검증 실패")
  void validateToken_expired_returnsFalse() {
    ReflectionTestUtils.setField(jwtProvider, "accessTokenExpiration", -1L);
    String token = jwtProvider.generateAccessToken(1L, "test@email.com");
    assertFalse(jwtProvider.validateToken(token));
  }

}
