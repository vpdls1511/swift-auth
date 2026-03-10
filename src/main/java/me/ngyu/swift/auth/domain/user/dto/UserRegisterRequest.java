package me.ngyu.swift.auth.domain.user.dto;

public record UserRegisterRequest(
  String email,
  String password,
  String name
) {}


