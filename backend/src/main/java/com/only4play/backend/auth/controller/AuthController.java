package com.only4play.backend.auth.controller;

import com.only4play.backend.auth.data.LoginRequest;
import com.only4play.backend.auth.service.AuthService;
import com.only4play.backend.users.data.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<?> login(
          HttpServletRequest request, HttpServletResponse response, @Valid @RequestBody LoginRequest body) {
    authService.login(request, response, body);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/me")
  public ResponseEntity<UserResponse> getSession(HttpServletRequest request) {
    return ResponseEntity.ok(authService.getSession(request));
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
    authService.logout(request, response);
    return ResponseEntity.ok().build();
  }

  /**
   * We don't have to do anything in this endpoint, the CsrfFilter will handle it.
   * This endpoint should be invoked by the frontend to get the CSRF token.
   * 浏览器发送的 OPTIONS 请求（预检请求）用于检查目标服务器是否允许这个跨域请求。
   * 服务器需要返回 Access-Control-Allow-Origin 头部，指示哪些源（origin）被允许发起请求。
   */
  @GetMapping("/csrf")
  public ResponseEntity<?> csrf() {
    return ResponseEntity.ok().build();
  }

}