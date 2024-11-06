package com.zerobase.together.dto;

import com.zerobase.together.entity.PostEntity;
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
public class PostDto {

  private Long postId;
  private Long coupleId;
  private Long userId;
  private String imgUrl;
  private String description;
  private LocalDateTime createdDateTime;
  private LocalDateTime modifiedDateTime;

  public static PostDto toDto(PostEntity postEntity) {
    return PostDto.builder()
        .postId(postEntity.getId())
        .coupleId(postEntity.getCoupleId())
        .userId(postEntity.getUserId())
        .imgUrl(postEntity.getImgUrl())
        .description(postEntity.getDescription())
        .createdDateTime(postEntity.getCreatedDateTime())
        .modifiedDateTime(postEntity.getModifiedDateTime())
        .build();
  }

}
