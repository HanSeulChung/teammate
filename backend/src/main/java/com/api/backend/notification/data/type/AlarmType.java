package com.api.backend.notification.data.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmType {
  DOCUMENTS("문서"),
  COMMENT("댓글"),
  EXIT_TEAM_PARTICIPANT("팀원 탈퇴"),
  KICKOUT("강퇴"),
  TEAM_DISBAND("강퇴"),
  MENTION("멘션"),
  INVITE("초대"),
  SCHEDULE_CREATE("스케쥴 생성"),
  SCHEDULE_DELETE("스케쥴 삭제");

  private final String description;

}
