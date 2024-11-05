package com.zerobase.together.dto;

import com.zerobase.together.entity.PostEntity;
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

  private Long coupleId;
  private Long userId;
  private String imgUrl;
  private String description;

  public static PostDto toDto(PostEntity postEntity) {
    return PostDto.builder()
        .coupleId(postEntity.getCoupleId())
        .userId(postEntity.getUserId())
        .imgUrl(postEntity.getImgUrl())
        .description(postEntity.getDescription())
        .build();
  }

}
