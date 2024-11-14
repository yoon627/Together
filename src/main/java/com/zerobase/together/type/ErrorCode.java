package com.zerobase.together.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  INVALID_PARTNER_NAME("존재하지 않는 파트너입니다."),
  INVALID_COUPLE_ID("존재하지 않는 커플아이디입니다."),
  INVALID_USER_NAME("존재하지 않는 유저명입니다."),
  INVALID_POST("해당 포스트가 존재하지 않습니다."),
  INVALID_COMMENT("해당 댓글이 존재하지 않습니다."),
  PASSWORD_UN_MATCH("비밀번호가 일치하지 않습니다."),
  USERNAME_ALREADY_EXISTS("이미 사용중인 유저명입니다."),
  PARTNER_ALREADY_COUPLE("상대방은 이미 커플입니다."),
  PARTNER_NOT_FOUND("상대방이 가입하지 않았습니다."),
  UNAUTHORIZED("권한이 없습니다."),
  INTERNAL_SERVER_ERROR("내부 서버 오류가 발생했습니다.");
  private final String description;
}
