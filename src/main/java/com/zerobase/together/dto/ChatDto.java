package com.zerobase.together.dto;

import com.zerobase.together.entity.ChatEntity;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto {

  private Long coupleId;
  private String senderId;
  private String imgUrl;
  private String content;
  private LocalDateTime createdDateTime;

  public static ChatDto toDto(ChatEntity chatEntity) {
    return ChatDto.builder()
        .coupleId(chatEntity.getCoupleId())
        .senderId(chatEntity.getSenderId())
        .imgUrl(chatEntity.getImgUrl())
        .content(chatEntity.getContent())
        .createdDateTime(chatEntity.getCreatedDateTime())
        .build();
  }
}
