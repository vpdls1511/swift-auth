package me.ngyu.swift.auth.domain.user.service;

import lombok.RequiredArgsConstructor;
import me.ngyu.swift.auth.domain.user.dto.TokenResponse;
import me.ngyu.swift.auth.domain.user.dto.UserDto;
import me.ngyu.swift.auth.domain.user.entity.User;
import me.ngyu.swift.auth.domain.user.repository.UserRepository;
import me.ngyu.swift.auth.global.jwt.JwtProvider;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;
  private final RedisTemplate<String, String> redisTemplate;

  public void register(UserDto.UserRegisterRequest request) {
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


  public TokenResponse login(UserDto.UserLoginRequest request) {
    User user = userRepository.findByEmail(request.email())
      .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

    if (!passwordEncoder.matches(request.password(), user.getPassword())) {
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }

    String accessToken = jwtProvider.generateAccessToken(user.getId(), user.getEmail());
    String refreshToken = jwtProvider.generateRefreshToken(user.getId());

    redisTemplate.opsForValue().set(
      "refresh:" + user.getId(),
      refreshToken,
      jwtProvider.getRefreshTokenExpiration(),
      TimeUnit.SECONDS
    );

    return new TokenResponse(accessToken, refreshToken);
  }
}
