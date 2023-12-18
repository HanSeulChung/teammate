package com.api.backend.notification.data.type;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SenderType {
  MEMBER("단일 회원"),
  MEMBERS("다수 회원"),
  TEAM_PARTICIPANTS("다수 팀원");

  private final String description;

}
