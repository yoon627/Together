package com.zerobase.together.dto;

import com.zerobase.together.entity.UserEntity;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

public class AuthDto {

  @Data
  @Builder
  public static class SignIn {

    private String username;
    private String password;
  }

  @Data
  @Builder
  public static class SignUp {

    private String username;
    private String password;
    private Long coupleId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private LocalDateTime removedDate;

    public UserEntity toEntity() {
      return UserEntity.builder()
          .username(this.username)
          .password(this.password)
          .coupleId(this.coupleId)
          .createdDateTime(this.createdDate)
          .modifiedDateTime(this.modifiedDate)
          .build();
    }
  }

  @Data
  @Builder
  public static class SignUpWithPartner {

    private String username;
    private String password;
    private String partnername;
    private Long coupleId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private LocalDateTime removedDate;


    public UserEntity toEntity() {
      return UserEntity.builder()
          .username(this.username)
          .password(this.password)
          .coupleId(this.coupleId)
          .createdDateTime(this.createdDate)
          .modifiedDateTime(this.modifiedDate)
          .build();
    }
  }
}
