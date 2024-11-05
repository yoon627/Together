package com.zerobase.together.dto;

import com.zerobase.together.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {

  private Long postId;
  private Long coupleId;
  private Long userId;
  private String description;

  public static CommentDto toDto(CommentEntity commentEntity) {
    return CommentDto.builder()
        .postId(commentEntity.getPostId())
        .coupleId(commentEntity.getCoupleId())
        .userId(commentEntity.getUserId())
        .description(commentEntity.getDescription())
        .build();
  }
}
