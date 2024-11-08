package com.zerobase.together.controller;

import com.zerobase.together.dto.AuthDto;
import com.zerobase.together.security.TokenProvider;
import com.zerobase.together.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;
  private final TokenProvider tokenProvider;

  /**
   * 회원 가입
   *
   * @param request (가입할 회원의 정보,권한)
   * @return 가입된 회원의 정보
   */
  @PostMapping("/signup")
  public ResponseEntity<?> signup(@RequestBody AuthDto.SignUp request) {
    var result = this.userService.register(request);
    return ResponseEntity.ok(result);
  }

  /**
   * 회원 가입
   *
   * @param request (가입할 회원의 정보,권한)
   * @return 가입된 회원의 정보
   */
  @PostMapping("/signupWithPartner")
  public ResponseEntity<?> signupWithPartner(@RequestBody AuthDto.SignUpWithPartner request) {
    var result = this.userService.registerWithPartner(request);
    return ResponseEntity.ok(result);
  }

  /**
   * 로그인
   *
   * @param request (로그인할 회원의 정보)
   * @return 1시간동안 유효한 jwt 토큰
   */
  @PostMapping("/signin")
  public ResponseEntity<?> signin(@RequestBody AuthDto.SignIn request) {
    var user = this.userService.authenticate(request);
    var token = this.tokenProvider.generateToken(user.getUsername());
    return ResponseEntity.ok(token);
  }
}
