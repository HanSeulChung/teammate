package com.api.backend.notification.data;

import java.util.Objects;

public class NotificationMessage {

  public static final String UPDATE_TEAM_PARTICIPANT_TEAM = "팀원이 추가 됐습니다.";

  public static String getDeleteDocumentName(String name) {
    if (Objects.isNull(name)) {
      throw new NullPointerException();
    }

    return "팀원이 '" + name + "' 문서를 삭제했습니다.";
  }

  public static String getCreateDocumentName(String name) {
    if (Objects.isNull(name)) {
      throw new NullPointerException();
    }

    return "팀원이 '" + name + "' 문서를 생성했습니다.";
  }

  public static String getDisbandTeamName(String name) {
    if (Objects.isNull(name)) {
      throw new NullPointerException();
    }

    return "팀이 '" + name + "' 해체됐습니다.";
  }

  public static String getTeamParticipantDeleteName(String name) {
    if (Objects.isNull(name)) {
      throw new NullPointerException();
    }

    return "해당 팀원'" + name + "' 탈퇴했습니다.";
  }

  public static String getCreateSchedule(String title) {
    if (Objects.isNull(title)) {
      throw new NullPointerException();
    }

    return "팀원이 '" + title + "' 일정을 만들었습니다.";
  }

  public static String getKickOutMessage(String teamName, String reason) {
    if (Objects.isNull(reason) || Objects.isNull(teamName)) {
      throw new NullPointerException();
    }

    return teamName + "팀 팀장이 해당 사유로 '" + reason + "' 강퇴 됐습니다.";
  }

  public static String getDeleteScheduleMessage(String title) {
    return "팀원이 " + title + " 일정을 삭제했습니다.";
  }
}