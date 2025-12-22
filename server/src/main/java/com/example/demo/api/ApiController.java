package com.example.demo.api;

import com.example.demo.api.dto.*;
import com.example.demo.auth.TokenService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;

import java.io.ByteArrayOutputStream;


@RestController
public class ApiController {

  private final TokenService tokenService;

  public ApiController(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest req) {
    if (req == null || req.username == null || req.password == null) {
      return ResponseEntity.badRequest().body("Bad request");
    }
    if (!req.username.equals("admin") || !req.password.equals("1234")) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong credentials");
    }

    String token = tokenService.issueToken(req.username);
    return ResponseEntity.ok(new LoginResponse(token));
  }

  @GetMapping("/image")
public ResponseEntity<byte[]> image(@RequestParam(defaultValue = "jpg") String format) throws IOException {
  String file = switch (format.toLowerCase()) {
    case "png" -> "images/sample.png";
    case "jpg", "jpeg" -> "images/sample.jpg";
    default -> null;
  };

  if (file == null) {
    return ResponseEntity.badRequest().build();
  }

  ClassPathResource res = new ClassPathResource(file);
  if (!res.exists()) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }

  byte[] bytes;
  try (InputStream is = res.getInputStream()) {
    bytes = is.readAllBytes();
  }

  MediaType type = format.equalsIgnoreCase("png") ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG;

  return ResponseEntity.ok()
      .contentType(type)
      .cacheControl(CacheControl.noCache())
      .body(bytes);
}

private static byte[] toByteArray(InputStream is) throws IOException {
  ByteArrayOutputStream buffer = new ByteArrayOutputStream();
  byte[] data = new byte[8192];
  int nRead;
  while ((nRead = is.read(data, 0, data.length)) != -1) {
    buffer.write(data, 0, nRead);
  }
  return buffer.toByteArray();
}

  @PostMapping(value = "/game", produces = "text/plain; charset=utf-8")
  public ResponseEntity<String> game(@RequestBody GameRequest req) {
    if (req == null || req.result == null) {
      return ResponseEntity.badRequest().body("result must be WIN/DRAW/LOSS");
    }
    return ResponseEntity.ok("Recorded result: " + req.result);
  }

  @DeleteMapping("/delete")
  public ResponseEntity<Void> delete(@RequestHeader(value = "Authorization", required = false) String auth) {
    String token = null;
    if (auth != null && auth.startsWith("Bearer ")) token = auth.substring("Bearer ".length()).trim();
    tokenService.revoke(token);
    return ResponseEntity.noContent().build();
  }
}