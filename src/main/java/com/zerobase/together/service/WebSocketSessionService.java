package com.zerobase.together.service;

import com.zerobase.together.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WebSocketSessionService {

  private final UserService userService;
  private final RedisTemplate<String, String> redisTemplate;
  private static final String PREFIX = "WS_SESSION:";

  // 사용자 접속 상태를 Redis에 저장
  public void addSession(String sessionId, String userId) {
    redisTemplate.opsForValue().set(PREFIX + sessionId, userId);
  }

  // 사용자 접속 해제 시 Redis에서 삭제
  public void removeSession(String sessionId) {
    redisTemplate.delete(PREFIX + sessionId);
  }

  // 로그인된 사용자 이름 기준으로 파트너 접속 확인
  public boolean isPartnerConnected() {
    UserDto user = this.userService.getLoginUser();
    return isUserConnected(this.userService.getPartnerName(user.getUsername()));
  }

  // 현재 접속 여부 확인
  public boolean isUserConnected(String username) {
    return redisTemplate.keys(PREFIX + "*").stream()
        .anyMatch(key -> redisTemplate.opsForValue().get(key).equals(username));
  }

}
