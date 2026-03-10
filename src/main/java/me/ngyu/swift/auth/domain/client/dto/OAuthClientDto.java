package me.ngyu.swift.auth.domain.client.dto;

public class OAuthClientDto {

  public record RegisterRequest(
    String name,
    String redirectUri,
    String scopes
  ) {}

  public record RegisterResponse(
    String clientId,
    String clientSecret
  ) {}
}
