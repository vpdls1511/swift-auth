package me.ngyu.swift.auth.domain.user.controller;

import static org.mockito.ArgumentMatchers.any;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.ngyu.swift.auth.domain.user.dto.TokenResponse;
import me.ngyu.swift.auth.domain.user.dto.UserDto;
import me.ngyu.swift.auth.domain.user.service.UserService;
import me.ngyu.swift.auth.global.jwt.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private UserService userService;

  @MockitoBean
  private JwtProvider jwtProvider;


  @Test
  @DisplayName("회원가입 성공 - 201 반환")
  void register_success() throws Exception {
    UserDto.UserRegisterRequest request = new UserDto.UserRegisterRequest("test@email.com", "password123!", "남규");

    mockMvc.perform(post("/api/users/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
      .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("이메일 중복 - 400 반환")
  void register_duplicateEmail_returns400() throws Exception {
    UserDto.UserRegisterRequest request = new UserDto.UserRegisterRequest("test@email.com", "password123!", "남규");

    doThrow(new IllegalArgumentException("이미 존재하는 이메일입니다."))
      .when(userService).register(any());

    mockMvc.perform(post("/api/users/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
      .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("로그인 성공 - 200 반환")
  void login_success() throws Exception {
    UserDto.UserLoginRequest request = new UserDto.UserLoginRequest("test@email.com", "password123!", "test_client");
    TokenResponse tokenResponse = new TokenResponse("accessToken", "refreshToken");

    when(userService.login(any())).thenReturn(tokenResponse);

    mockMvc.perform(post("/api/users/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.accessToken").value("accessToken"))
      .andExpect(jsonPath("$.refreshToken").value("refreshToken"));
  }

  @Test
  @DisplayName("로그인 실패 - 400 반환")
  void login_fail_returns400() throws Exception {
    UserDto.UserLoginRequest request = new UserDto.UserLoginRequest("test@email.com", "wrongPassword!", "test_client");

    doThrow(new IllegalArgumentException("비밀번호가 일치하지 않습니다."))
      .when(userService).login(any());

    mockMvc.perform(post("/api/users/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
      .andExpect(status().isBadRequest());
  }
}
