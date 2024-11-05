package com.zerobase.together.entity;

import com.zerobase.together.type.HistoryAction;
import com.zerobase.together.type.HistoryTarget;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "history")
@EntityListeners(AuditingEntityListener.class)
public class HistoryEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long coupleId;
  private Long userId;
  private Long targetId;
  @Enumerated(EnumType.STRING)
  private HistoryTarget historyTarget;
  @Enumerated(EnumType.STRING)
  private HistoryAction historyAction;
  @CreatedDate
  private LocalDateTime createdDateTime;

}
