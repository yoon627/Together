package com.zerobase.together.dto;

import com.zerobase.together.entity.HistoryEntity;
import com.zerobase.together.type.HistoryAction;
import com.zerobase.together.type.HistoryTarget;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class HistoryDto {

  private Long historyId;
  private Long coupleId;
  private Long userId;
  private Long targetId;
  @Enumerated(EnumType.STRING)
  private HistoryTarget historyTarget;
  @Enumerated(EnumType.STRING)
  private HistoryAction historyAction;
  private String postContent;
  private String commentContent;
  private LocalDateTime createdDateTime;

  public static HistoryDto toDto(HistoryEntity historyEntity) {
    return HistoryDto.builder()
        .historyId(historyEntity.getId())
        .coupleId(historyEntity.getCoupleId())
        .userId(historyEntity.getUserId())
        .targetId(historyEntity.getTargetId())
        .historyTarget(historyEntity.getHistoryTarget())
        .historyAction(historyEntity.getHistoryAction())
        .postContent(historyEntity.getPostContent())
        .commentContent(historyEntity.getCommentContent())
        .createdDateTime(historyEntity.getCreatedDateTime())
        .build();
  }
}
