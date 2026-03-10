package me.ngyu.swift.auth.domain.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.ngyu.swift.auth.domain.client.dto.OAuthClientDto;
import me.ngyu.swift.auth.domain.client.service.OAuthClientService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OAuthClientController.class)
@AutoConfigureMockMvc(addFilters = false)
class OAuthClientControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private OAuthClientService oAuthClientService;

  @Test
  @DisplayName("클라이언트 등록 성공 - 201 반환")
  void register_success() throws Exception {
    OAuthClientDto.RegisterRequest request = new OAuthClientDto.RegisterRequest("My Project", "https://myapp.com/callback", "read,write");
    OAuthClientDto.RegisterResponse response = new OAuthClientDto.RegisterResponse("clientId", "clientSecret");

    when(oAuthClientService.register(any())).thenReturn(response);

    mockMvc.perform(post("/api/clients/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.clientId").value("clientId"))
      .andExpect(jsonPath("$.clientSecret").value("clientSecret"));
  }
}
