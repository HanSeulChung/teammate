package com.api.backend.global.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  // valid
  PARAMETER_NOT_VALID_EXCEPTION(400, "잘못된 파라미터 값 입니다."),
  INVALID_MESSAGE_EXCEPTION(500, "잘못된 메세지 값입니다."),

  ENUM_NOT_VALID_EXCEPTION(500, "잘못된 ENUM요청이 들어왔습니다."),
  ENUM_NOT_FOUND_EXCEPTION(500, "비어있는 ENUM입니다."),
  SCHEDULE_CATEGORY_NOT_FOUND_EXCEPTION(500, "존재하지 않는 일정 카테고리 입니다."),
  SCHEDULE_CATEGORY_ALREADY_EXIST_EXCEPTION(500, "이미 존재하는 일정 카테고리 입니다."),
  SCHEDULE_NOT_FOUND_EXCEPTION(500, "존재하지 않는 일정입니다."),
  INVALID_REPEAT_CYCLE_EXCEPTION(400, "잘못된 반복 주기입니다."),
  NON_REPEATING_SCHEDULE_EXCEPTION(500, "반복일정이 아닙니다."),
  TEAM_PARTICIPANTS_ID_DUPLICATE_EXCEPTION(400, "일정 팀참가자번호는 중복될 수 없습니다."),
  SCHEDULE_DELETE_PERMISSION_DENIED_EXCEPTION(400, "일정을 삭제할 권한이 없습니다."),
  SCHEDULE_CREATOR_EXISTS_EXCEPTION(400, "일정 생성자가 팀안에 존재하므로, 팀장권한으로 삭제가 불가능합니다."),

  // team
  TEAM_NOT_FOUND_EXCEPTION(500, "존재하지 않는 팀입니다."),
  TEAM_CODE_NOT_VALID_EXCEPTION(500, "팀 코드가 일치하지 않습니다."),
  TEAM_NOT_EQUALS_EXCEPTION(500, "팀이 일치하지 않습니다."),
  TEAM_IS_DELETE_TRUE_EXCEPTION(500, "해당 팀 이미 해체된 팀입니다."),
  TEAM_IS_DELETEING_EXCEPTION(500, "해당 팀 이미 해체 중에 있습니다."),
  TEAM_NOT_DELETEING_EXCEPTION(500, "해당 팀은 해체 중이 아닙니다."),
  TEAM_LIMIT_VALID_EXCEPTION(500, "해당 팀의 인원제한 허용치를 넘었습니다."),
  TEAM_RESTORE_EXPIRED_EXCEPTION(500, "해당 팀은 해체 기한이 지났습니다."),

  // teamParticipant
  TEAM_PARTICIPANT_DELETE_NOT_VALID_EXCEPTION(500, "팀장 권한을 위임해야지 팀 탈퇴가 가능합니다."),
  TEAM_PARTICIPANT_NOT_VALID_READER_EXCEPTION(500, "팀장 권한이 있어야 합니다."),
  TEAM_PARTICIPANT_EQUALS_EXCEPTION(500, "동일한 회원을 삭제할려고 하고 있습니다."),
  TEAM_PARTICIPANT_NOT_VALID_MATE_EXCEPTION(500, "팀원 권한이 있어야 합니다."),
  TEAM_PARTICIPANTS_EXIST_EXCEPTION(500, "이미 해당 팀의 일원입니다."),
  TEAM_PARTICIPANTS_EQUALS_EXCEPTION(500, "나 자신을 가리키고 있습니다.."),
  TEAM_PARTICIPANTS_NOT_LEADER_EXCEPTION(500, "팀 해체는 리더만 가능합니다."),
  TEAM_PARTICIPANTS_NOT_FOUND_EXCEPTION(500, "존재하지 않는 팀원입니다."),
  TEAM_PARTICIPANTS_NOT_VALID_EXCEPTION(500, "해당 팀의 팀원이 아닙니다."),

  // notification
  NOTIFICATION_NOT_FOUND_EXCEPTION(500, "존재하지 않는 알람입니다."),
  NOTIFICATION_IS_READ_TRUE_EXCEPTION(500, "이미 읽은 알람입니다."),
  NOTIFICATION_NOT_VALID_EXCEPTION(500, "읽을 권한이 없는 사용자입니다."),

  MEMBER_NOT_FOUND_EXCEPTION(500, "존재하지 않는 회원입니다."),
  MEMBER_NOT_EQUALS_EXCEPTION(500, "회원 정보가 일치하지 않습니다."),
  TOKEN_EXPIRED_EXCEPTION(500, "토큰이 만료 되었습니다."),
  EMAIL_NOT_FOUND_EXCEPTION(400, "해당 이메일로 가입한 사용자가 존재하지 않습니다."),
  EMAIL_ALREADY_EXIST_EXCEPTION(409,"해당 이메일로 가입한 사용자가 존재합니다."),
  PASSWORD_NOT_MATCH_EXCEPTION(400, "비밀번호가 일치하지 않습니다."),
  TOKEN_NOT_FOUND_PERMISSION_INFORMATION(400,"권한 정보가 없는 토큰입니다."),
  TOKEN_INVALID_EXCEPTION(400,"유효하지 않는 토큰입니다."),
  MEMBER_NOT_MATCH_PASSWORD_EXCEPTION(400, "기존 비밀번호가 틀렸습니다."),
  INCORRECT_FORM_NEW_PASSWORD_EXCEPTION(400,"새로운 비밀번호 형식이 잘못되었습니다."),
  NOT_MATCH_NEW_PASSWORD_EXCEPTION(400,"새로운 비밀번호가 재확인 비밀번호와 일치하지 않습니다."),


  TARGET_URL_EMPTY_EXCEPTION(400, "비어 있는 반환 url이 있습니다."),

  //email
  EMAIL_NOT_TRANSFER_EXCEPTION(400,"이메일 전송에 실패했습니다."),
  EMAIL_NOT_VERIFICATION_EXCEPTION(400, "이메일 인증이 되지 않았습니다."),

  // principal
  PRINCIPAL_IS_NULL(400, "인증이 되지 않았습니다."),

  // documents
  DOCUMENT_NOT_IN_TEAM_EXCEPTION(400, "해당 팀에 속하지 않은 문서입니다."),
  DOCUMENT_WRITER_UNMATCH_TEAM_PARTICIPANTS_EXCEPTION(400, "해당 문서의 생성자가 아닙니다."),
  DOCUMENT_WRITER_EXISTS_EXCEPTION(400, "문서의 생성자가 팀안에 존재하므로 팀장권한으로 삭제가 불가능합니다."),
  DOCUMENT_NOT_FOUND_EXCEPTION(400, "존재하지 않는 문서입니다."),
  DOCUMENT_ID_AND_TEAM_ID_NOT_FOUND_EXCEPTION(400, "팀ID , 문서ID가 존재하지 않습니다."),

  // comment
  COMMENT_NOT_FOUND_EXCEPTION(400, "존재하지 않는 댓글 입니다."),
  COMMENT_UNMATCH_WRITER_ID_EXCEPTION(400, "댓글의 작성자가 아닙니다."),

  // image
  IMG_FILE_VOLUMNE_TOO_BIG_EXCEPTION(400, "이미지 용량이 제한 용량(10MB)보다 큽니다."),
  IMG_FILE_NOT_UPLOAD_EXCEPTION(400, "이미지 파일이 업로드 되지 않았습니다.");

  private final int code;
  private final String errorMessage;
}

