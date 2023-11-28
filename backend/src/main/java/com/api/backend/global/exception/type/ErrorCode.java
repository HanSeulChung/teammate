package com.api.backend.global.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  ENUM_NOT_VALID_EXCEPTION(500,"잘못된 ENUM요청이 들어왔습니다."),
  ENUM_NOT_FOUND_EXCEPTION(500,"비어있는 ENUM입니다."),

  EMAIL_ALREADY_EXIST_EXCEPTION(409,"해당 이메일로 가입한 사용자가 존재합니다."),
  PASSWORD_NOT_MATCH_EXCEPTION(400, "비밀번호가 일치하지 않습니다.");

  private final int code;
  private final String errorMessage;
}

