package me.ngyu.swift.auth.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ngyu.swift.auth.domain.client.entity.OAuthClient;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_client_consent", schema = "usr")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserClientConsent {

  @EmbeddedId
  private UserClientConsentId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("userId")
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("clientId")
  @JoinColumn(name = "client_id")
  private OAuthClient oAuthClient;

  @Column(nullable = false)
  private String scopes;

  @Column(nullable = false, updatable = false)
  private LocalDateTime consentedAt;

  @Builder
  public UserClientConsent(User user, OAuthClient oAuthClient, String scopes) {
    this.id = new UserClientConsentId(user.getId(), oAuthClient.getId());
    this.user = user;
    this.oAuthClient = oAuthClient;
    this.scopes = scopes;
    this.consentedAt = LocalDateTime.now();
  }
}
