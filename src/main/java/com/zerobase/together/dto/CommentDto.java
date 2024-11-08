package com.zerobase.together.dto;

import com.zerobase.together.entity.CommentEntity;
import java.time.LocalDateTime;
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

  private Long commentId;
  private Long postId;
  private Long coupleId;
  private Long userId;
  private String description;
  private LocalDateTime createdDateTime;
  private LocalDateTime modifiedDateTime;
  private LocalDateTime deletedDateTime;

  public static CommentDto toDto(CommentEntity commentEntity) {
    return CommentDto.builder()
        .commentId(commentEntity.getId())
        .postId(commentEntity.getPostId())
        .coupleId(commentEntity.getCoupleId())
        .userId(commentEntity.getUserId())
        .description(commentEntity.getDescription())
        .createdDateTime(commentEntity.getCreatedDateTime())
        .modifiedDateTime(commentEntity.getModifiedDateTime())
        .deletedDateTime(commentEntity.getDeletedDateTime())
        .build();
  }
}
