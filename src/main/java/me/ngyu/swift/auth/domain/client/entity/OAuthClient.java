package me.ngyu.swift.auth.domain.client.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "oauth_client")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthClient {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String clientId;

  @Column(nullable = false)
  private String clientSecret;

  @Column(nullable = false)
  private String name;

  @Column
  private String redirectUri;

  @Column(nullable = false)
  private String scopes;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Builder
  public OAuthClient(String clientId, String clientSecret, String name, String redirectUri, String scopes) {
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.name = name;
    this.redirectUri = redirectUri;
    this.scopes = scopes;
    this.createdAt = LocalDateTime.now();
  }
}
