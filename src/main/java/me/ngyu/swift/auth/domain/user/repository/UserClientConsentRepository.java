package me.ngyu.swift.auth.domain.user.repository;

import me.ngyu.swift.auth.domain.client.entity.OAuthClient;
import me.ngyu.swift.auth.domain.user.entity.User;
import me.ngyu.swift.auth.domain.user.entity.UserClientConsent;
import me.ngyu.swift.auth.domain.user.entity.UserClientConsentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserClientConsentRepository extends JpaRepository<UserClientConsent, UserClientConsentId> {
  boolean existsByUserAndOAuthClient(User user, OAuthClient oAuthClient);
}
