package com.api.backend.global.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  ENUM_NOT_VALID_EXCEPTION(500, "잘못된 ENUM요청이 들어왔습니다."),
  ENUM_NOT_FOUND_EXCEPTION(500, "비어있는 ENUM입니다."),
  SCHEDULE_CATEGORY_NOT_FOUND_EXCEPTION(500, "존재하지 않는 일정 카테고리 입니다."),
  SCHEDULE_CATEGORY_ALREADY_EXIST_EXCEPTION(500, "이미 존재하는 일정 카테고리 입니다."),
  SCHEDULE_NOT_FOUND_EXCEPTION(500, "존재하지 않는 일정입니다."),
  INVALID_REPEAT_CYCLE_EXCEPTION(400, "잘못된 반복 주기입니다."),
  TEAM_NOT_FOUND_EXCEPTION(500, "존재하지 않는 팀입니다."),
  TEAM_CODE_NOT_VALID_EXCEPTION(500, "팀 코드가 일치하지 않습니다."),
  TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION(500, "존재하지 않는 팀원입니다."),
  TEAM_PARTICIPANTS_NOT_VALID_EXCEPTION(500, "해당 팀의 팀원이 아닙니다."),
  TEAM_PARTICIPANTS_EXIST_EXCEPTION(500, "이미 해당 팀의 일원입니다."),
  MEMBER_NOT_FOUND_EXCEPTION(500, "존재하지 않는 회원입니다."),
  TOKEN_EXPIRED_EXCEPTION(500, "토큰이 만료 되었습니다."),
  EMAIL_NOT_FOUND_EXCEPTION(400, "해당 이메일로 가입한 사용자가 존재하지 않습니다."),
  EMAIL_ALREADY_EXIST_EXCEPTION(409,"해당 이메일로 가입한 사용자가 존재합니다."),
  PASSWORD_NOT_MATCH_EXCEPTION(400, "비밀번호가 일치하지 않습니다."),
  TOKEN_NOT_FOUND_PERMISSION_INFORMATION(400,"권한 정보가 없는 토큰입니다.");
  private final int code;
  private final String errorMessage;
};
