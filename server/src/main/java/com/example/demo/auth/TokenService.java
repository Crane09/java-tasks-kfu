package com.example.demo.auth;

import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {
  private final ConcurrentHashMap<String, String> tokenToUser = new ConcurrentHashMap<>();

  public String issueToken(String username) {
    String token = UUID.randomUUID().toString();
    tokenToUser.put(token, username);
    return token;
  }

  public boolean isValid(String token) {
    return token != null && tokenToUser.containsKey(token);
  }

  public String getUser(String token) {
    return tokenToUser.get(token);
  }

  public void revoke(String token) {
    if (token != null) tokenToUser.remove(token);
  }
}