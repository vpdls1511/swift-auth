package me.ngyu.swift.auth.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.access-token-expiration}")
  private long accessTokenExpiration;

  @Getter
  @Value("${jwt.refresh-token-expiration}")
  private long refreshTokenExpiration;

  public String generateAccessToken(Long userId, String email) {
    return Jwts.builder()
      .subject(String.valueOf(userId))
      .claim("email", email)
      .issuedAt(new Date())
      .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration * 1000))
      .signWith(getSigningKey())
      .compact();
  }

  public String generateRefreshToken(Long userId) {
    return Jwts.builder()
      .subject(String.valueOf(userId))
      .issuedAt(new Date())
      .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiration * 1000))
      .signWith(getSigningKey())
      .compact();
  }

  public Long extractUserId(String token) {
    return Long.parseLong(getClaims(token).getSubject());
  }

  public boolean validateToken(String token) {
    try {
      getClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  private Claims getClaims(String token) {
    return Jwts.parser()
      .verifyWith(getSigningKey())
      .build()
      .parseSignedClaims(token)
      .getPayload();
  }

  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

}
