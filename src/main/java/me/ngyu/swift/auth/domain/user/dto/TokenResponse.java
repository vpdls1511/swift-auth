package me.ngyu.swift.auth.domain.user.dto;

public record TokenResponse(String accessToken,
                            String refreshToken) {
}
