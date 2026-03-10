package me.ngyu.swift.auth.domain.user.service;

import lombok.RequiredArgsConstructor;
import me.ngyu.swift.auth.domain.user.dto.UserRegisterRequest;
import me.ngyu.swift.auth.domain.user.entity.User;
import me.ngyu.swift.auth.domain.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public void register(UserRegisterRequest request) {
    if (userRepository.existsByEmail(request.email())) {
      throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
    }

    User user = User.builder()
      .email(request.email())
      .password(passwordEncoder.encode(request.password()))
      .name(request.name())
      .build();

    userRepository.save(user);
  }
}
