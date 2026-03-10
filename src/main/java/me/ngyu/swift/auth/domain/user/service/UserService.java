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

  public UserDto.UserResponse getMyInfo(Long userId) {
    User user = userRepository.findById(userId)
      .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
    return new UserDto.UserResponse(user.getId(), user.getEmail(), user.getName());
  }

  public TokenResponse refresh(String refreshToken) {
    if (!jwtProvider.validateToken(refreshToken)) {
      throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
    }

    Long userId = jwtProvider.extractUserId(refreshToken);
    String stored = redisTemplate.opsForValue().get("refresh:" + userId);

    if (!refreshToken.equals(stored)) {
      throw new IllegalArgumentException("토큰이 일치하지 않습니다.");
    }

    User user = userRepository.findById(userId)
      .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

    String newAccessToken = jwtProvider.generateAccessToken(user.getId(), user.getEmail());
    String newRefreshToken = jwtProvider.generateRefreshToken(user.getId());

    redisTemplate.opsForValue().set(
      "refresh:" + userId,
      newRefreshToken,
      jwtProvider.getRefreshTokenExpiration(),
      TimeUnit.SECONDS
    );

    return new TokenResponse(newAccessToken, newRefreshToken);
  }
}
