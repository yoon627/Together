package com.zerobase.together.model;

import com.zerobase.together.persist.entity.UserEntity;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

public class Auth {

  @Data
  public static class SignIn {

    private String userId;
    private String password;
  }

  @Data
  public static class SignUp {

    private String userId;
    private String password;
    private Long coupleId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private LocalDateTime removedDate;

    public UserEntity toEntity() {
      return UserEntity.builder()
          .userId(this.userId)
          .password(this.password)
          .coupleId(this.coupleId)
          .createdDate(this.createdDate)
          .modifiedDate(this.modifiedDate)
          .removedDate(this.removedDate)
          .build();
    }
  }

  @Data
  public static class SignUpWithPartner {

    private String userId;
    private String password;
    private String partnerId;
    private Long coupleId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private LocalDateTime removedDate;


    public UserEntity toEntity() {
      return UserEntity.builder()
          .userId(this.userId)
          .password(this.password)
          .coupleId(this.coupleId)
          .createdDate(this.createdDate)
          .modifiedDate(this.modifiedDate)
          .removedDate(this.removedDate)
          .build();
    }
  }
}
