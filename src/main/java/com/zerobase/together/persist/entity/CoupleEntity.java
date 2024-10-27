package com.zerobase.together.persist.entity;

import jakarta.persistence.Entity;
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

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "couple")
public class CoupleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long coupleId;
  private LocalDateTime createdDate;
  private LocalDateTime modifiedDate;
  private LocalDateTime removedDate;
  private boolean authorized;

}
