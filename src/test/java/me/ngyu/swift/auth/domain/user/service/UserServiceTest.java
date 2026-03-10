package me.ngyu.swift.auth.domain.user.service;

import me.ngyu.swift.auth.domain.user.dto.TokenResponse;
import me.ngyu.swift.auth.domain.user.dto.UserDto;
import me.ngyu.swift.auth.domain.user.repository.UserRepository;
import me.ngyu.swift.auth.domain.user.entity.User;
import me.ngyu.swift.auth.global.jwt.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private JwtProvider jwtProvider;

  @Mock
  private RedisTemplate<String, String> redisTemplate;

  @Test
  @DisplayName("회원가입 성공")
  void register_success() {
    // given
    UserDto.UserRegisterRequest request = new UserDto.UserRegisterRequest("test@email.com", "password123!", "남규");
    when(userRepository.existsByEmail(request.email())).thenReturn(false);
    when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");

    // when
    userService.register(request);

    // then
    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  @DisplayName("이미 존재하는 이메일로 회원가입 시 예외 발생")
  void register_duplicateEmail_throwsException() {
    // given
    UserDto.UserRegisterRequest request = new UserDto.UserRegisterRequest("test@email.com", "password123!", "남규");
    when(userRepository.existsByEmail(request.email())).thenReturn(true);

    // when & then
    assertThrows(IllegalArgumentException.class, () -> userService.register(request));
  }

  @Test
  @DisplayName("로그인 성공 - 토큰 반환")
  void login_success() {
    // given
    UserDto.UserLoginRequest request = new UserDto.UserLoginRequest("test@email.com", "password123!");
    User user = User.builder()
      .email("test@email.com")
      .password("encodedPassword")
      .name("남규")
      .build();

    @SuppressWarnings("unchecked")
    ValueOperations<String, String> valueOps = mock(ValueOperations.class);
    when(redisTemplate.opsForValue()).thenReturn(valueOps);
    when(jwtProvider.generateAccessToken(any(), any())).thenReturn("accessToken");
    when(jwtProvider.generateRefreshToken(any())).thenReturn("refreshToken");

    when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(true);

    // when
    TokenResponse response = userService.login(request);

    // then
    assertNotNull(response.accessToken());
    assertNotNull(response.refreshToken());
  }

  @Test
  @DisplayName("존재하지 않는 이메일 로그인 시 예외 발생")
  void login_userNotFound_throwsException() {
    // given
    UserDto.UserLoginRequest request = new UserDto.UserLoginRequest("notfound@email.com", "password123!");
    when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

    // when & then
    assertThrows(IllegalArgumentException.class, () -> userService.login(request));
  }

  @Test
  @DisplayName("비밀번호 불일치 시 예외 발생")
  void login_wrongPassword_throwsException() {
    // given
    UserDto.UserLoginRequest request = new UserDto.UserLoginRequest("test@email.com", "wrongPassword!");
    User user = User.builder()
      .email("test@email.com")
      .password("encodedPassword")
      .name("남규")
      .build();

    when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(false);

    // when & then
    assertThrows(IllegalArgumentException.class, () -> userService.login(request));
  }

}
