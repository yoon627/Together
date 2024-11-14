package com.zerobase.together.component;

import com.zerobase.together.service.UserService;
import com.zerobase.together.service.WebSocketSessionService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@AllArgsConstructor
public class WebSocketEventListener {

  private WebSocketSessionService webSocketSessionService;
  private UserService userService;

  // 연결 시 이벤트
  @EventListener
  public void handleWebSocketConnectListener(SessionConnectEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String sessionId = headerAccessor.getSessionId();
    String username = this.userService.getUsername();
    if (username != null) {
      webSocketSessionService.addSession(sessionId, username);
    }
  }

  // 연결 해제 시 이벤트
  @EventListener
  public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
    String sessionId = event.getSessionId();
    webSocketSessionService.removeSession(sessionId);
  }

}
