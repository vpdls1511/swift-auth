package me.ngyu.swift.auth.domain.user.dto;

public class UserDto {
  public record UserRegisterRequest(String email,
                                    String password,
                                    String name
  ) {
  }

  public record UserLoginRequest(String email,
                                 String password,
                                 String clientId
  ) {
  }

  public record UserResponse(Long id,
                             String email,
                             String name
  ) {
  }

}

