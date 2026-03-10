package me.ngyu.swift.auth.domain.user.service;

import me.ngyu.swift.auth.domain.user.repository.UserRepository;
import me.ngyu.swift.auth.domain.user.dto.UserRegisterRequest;
import me.ngyu.swift.auth.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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

  @Test
  @DisplayName("회원가입 성공")
  void register_success() {
    // given
    UserRegisterRequest request = new UserRegisterRequest("test@email.com", "password123!", "남규");
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
    UserRegisterRequest request = new UserRegisterRequest("test@email.com", "password123!", "남규");
    when(userRepository.existsByEmail(request.email())).thenReturn(true);

    // when & then
    assertThrows(IllegalArgumentException.class, () -> userService.register(request));
  }
}
