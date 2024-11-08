package com.zerobase.together.controller;

import com.zerobase.together.dto.ChatDto;
import com.zerobase.together.service.ChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ChatController {

  private final SimpMessagingTemplate messagingTemplate;
  private final ChatService chatService;

  @MessageMapping("/send") // 클라이언트가 /app/send로 보낸 메시지를 수신
  @SendTo("/topic/messages") // 구독자에게 /topic/messages로 메시지를 브로드캐스트
  public void sendMessage(ChatDto message) {
    String destination = "/topic/messages/" + message.getCoupleId();
    this.messagingTemplate.convertAndSend(destination, message);
    this.chatService.saveChat(message);
  }

  @GetMapping("/chats/{pageNum}")
  public ResponseEntity<List<ChatDto>> getChats(@PathVariable Integer pageNum) {
    var result = this.chatService.getChats(pageNum);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/chat")
  public String chatPage() {
    return "chat.html";
  }
}