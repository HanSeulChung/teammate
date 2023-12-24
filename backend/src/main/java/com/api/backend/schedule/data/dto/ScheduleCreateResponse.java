package com.api.backend.schedule.data.dto;

import com.api.backend.notification.data.NotificationMessage;
import com.api.backend.notification.data.type.AlarmType;
import com.api.backend.notification.transfers.MentionTeamParticipantsNotifyByDto;
import com.api.backend.schedule.data.type.RepeatCycle;
import com.api.backend.team.data.type.TeamRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ScheduleCreateResponse implements MentionTeamParticipantsNotifyByDto {
  private Long scheduleId;
  private String scheduleType;
  private String categoryName;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime startDt;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime endDt;
  private String title;
  private String content;
  private String place;
  private String color;
  private RepeatCycle repeatCycle;
  private String month;
  private int day;
  private String dayOfWeek;
  private List<Long> teamParticipantsIds;
  private List<String> teamParticipantsNames;
  private List<TeamRole> teamRoles;

  // 알람
  private Long createParticipantsId;
  private String createParticipantsNickName;
  private Long teamId;

  public static ScheduleCreateResponse from(RepeatScheduleResponse repeatScheduleResponse) {
    return ScheduleCreateResponse.builder()
        .scheduleId(repeatScheduleResponse.getScheduleId())
        .scheduleType("반복 일정")
        .categoryName(repeatScheduleResponse.getCategoryName())
        .startDt(repeatScheduleResponse.getStartDt())
        .endDt(repeatScheduleResponse.getEndDt())
        .title(repeatScheduleResponse.getTitle())
        .content(repeatScheduleResponse.getContent())
        .place(repeatScheduleResponse.getPlace())
        .repeatCycle(repeatScheduleResponse.getRepeatCycle())
        .month(repeatScheduleResponse.getMonth())
        .day(repeatScheduleResponse.getDay())
        .dayOfWeek(repeatScheduleResponse.getDayOfWeek())
        .teamParticipantsIds(repeatScheduleResponse.getTeamParticipantsIds())
        .teamParticipantsNames(repeatScheduleResponse.getTeamParticipantsNames())
        .teamRoles(repeatScheduleResponse.getTeamRoles())
        .build();
  }

  public static ScheduleCreateResponse from(SimpleScheduleResponse simpleScheduleResponse) {
    return ScheduleCreateResponse.builder()
        .scheduleId(simpleScheduleResponse.getScheduleId())
        .scheduleType("단순 일정")
        .categoryName(simpleScheduleResponse.getCategoryName())
        .scheduleId(simpleScheduleResponse.getScheduleId())
        .startDt(simpleScheduleResponse.getStartDt())
        .endDt(simpleScheduleResponse.getEndDt())
        .title(simpleScheduleResponse.getTitle())
        .content(simpleScheduleResponse.getContent())
        .place(simpleScheduleResponse.getPlace())
        .teamParticipantsIds(simpleScheduleResponse.getTeamParticipantsIds())
        .teamParticipantsNames(simpleScheduleResponse.getTeamParticipantsNames())
        .teamRoles(simpleScheduleResponse.getTeamRoles())
        .build();
  }


  public void AddAlarmValue(Long teamId, Long teamParticipantsId,String nickName) {
    this.teamId = teamId;
    this.createParticipantsId = teamParticipantsId;
    this.createParticipantsNickName = nickName;
  }

  @Override
  public List<Long> getMentionTeamParticipantIds() {
    return teamParticipantsIds;
  }

  @Override
  public Long getExcludeTeamParticipantId() {
    return createParticipantsId;
  }

  @Override
  public AlarmType getAlarmType() {
    return AlarmType.SCHEDULE_CREATE;
  }

  @Override
  public String getSendMessage() {
    return NotificationMessage.getCreateSchedule(title);
  }

  @Override
  public String getTeamParticipantsNickName() {
    return createParticipantsNickName;
  }
}
