package com.zerobase.together.service;

import com.zerobase.together.dto.ChatDto;
import com.zerobase.together.dto.UserDto;
import com.zerobase.together.entity.ChatEntity;
import com.zerobase.together.repository.ChatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

  private final ChatRepository chatRepository;

  public ChatDto saveChat(ChatDto chatDto) {
    this.chatRepository.save(ChatEntity.builder()
        .coupleId(chatDto.getCoupleId())
        .senderId(chatDto.getSenderId())
        .content(chatDto.getContent())
        .build());
    return chatDto;
  }

  public List<ChatDto> getChats(int pageNum) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    UserDto userDto = (UserDto) userDetails;
    Long coupleId = userDto.getCoupleId();
    Pageable pageable = PageRequest.of(pageNum, 20);
    Page<ChatEntity> result = this.chatRepository.findAllByCoupleIdOrderByCreatedDateTimeDesc(
        coupleId, pageable);
    return result.stream().map(ChatDto::toDto).toList();
  }
}
