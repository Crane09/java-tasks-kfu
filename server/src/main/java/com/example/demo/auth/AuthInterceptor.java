package com.example.demo.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

  private final TokenService tokenService;

  public AuthInterceptor(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String path = request.getRequestURI();
    if (path.equals("/login")) return true;

    String header = request.getHeader("Authorization");
    String token = extractBearerToken(header);

    if (!tokenService.isValid(token)) {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.setContentType("text/plain; charset=utf-8");
      response.getWriter().write("Unauthorized: missing/invalid token");
      return false;
    }
    return true;
  }

  private String extractBearerToken(String header) {
    if (header == null) return null;
    String prefix = "Bearer ";
    if (!header.startsWith(prefix)) return null;
    return header.substring(prefix.length()).trim();
  }
}