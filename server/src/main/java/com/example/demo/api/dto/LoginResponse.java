package com.example.demo.api.dto;

public class LoginResponse {
  public String token;
  public String tokenType;

  public LoginResponse(String token) {
    this.token = token;
    this.tokenType = "Bearer";
  }
}