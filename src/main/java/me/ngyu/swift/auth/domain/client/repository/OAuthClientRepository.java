package me.ngyu.swift.auth.domain.client.repository;

import me.ngyu.swift.auth.domain.client.entity.OAuthClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuthClientRepository extends JpaRepository<OAuthClient, Long> {
  Optional<OAuthClient> findByClientId(String clientId);
}
