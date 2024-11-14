package com.zerobase.together.dto;

import com.zerobase.together.entity.PhotoEntity;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PhotoDto {

  private Long photoId;
  private Long coupleId;
  private Long userId;
  private String imgUrl;
  private LocalDateTime createdDateTime;

  public static PhotoDto toDto(PhotoEntity photoEntity, String urlPrefix) {
    return PhotoDto.builder()
        .photoId(photoEntity.getId())
        .coupleId(photoEntity.getCoupleId())
        .userId(photoEntity.getUserId())
        .imgUrl(urlPrefix + photoEntity.getImgUrl())
        .createdDateTime(photoEntity.getCreatedDateTime())
        .build();
  }
}
