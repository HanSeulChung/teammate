package com.api.backend.team.data.dto;

import static com.api.backend.notification.data.NotificationMessage.DISBAND_TEAM;
import static com.api.backend.team.data.ResponseMessage.DISBANDING_TEAM;

import com.api.backend.notification.data.dto.DtoValueExtractor;
import com.api.backend.notification.data.type.AlarmType;
import com.api.backend.notification.data.type.SenderType;
import com.api.backend.team.data.entity.Team;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TeamDisbandResponse implements DtoValueExtractor {
  @NotNull(message = "teamId는 비어있는 값입니다.")
  private Long teamId;
  private Long excludeMemberId;
  private String teamName;
  private LocalDate reservationDt;
  @NotNull(message = "비밀번호를 입력해주세요")
  private String message;


  public static TeamDisbandResponse from(Team team, Long memberId) {
    return TeamDisbandResponse.builder()
        .teamId(team.getTeamId())
        .excludeMemberId(memberId)
        .teamName(team.getName())
        .reservationDt(team.getRestorationDt())
        .message(DISBANDING_TEAM).build();
  }

  @Override
  public Long getMemberId() {
    return null;
  }

  @Override
  public Long getExcludeTeamParticipantId() {
    return null;
  }

  @Override
  public SenderType getSenderType() {
    return SenderType.MEMBERS;
  }

  @Override
  public AlarmType getAlarmType() {
    return AlarmType.TEAM_DISBAND;
  }

  @Override
  public String getTeamNameOrTeamParticipantNickName() {
    return teamName;
  }

  @Override
  public String getUrl() {
    return null;
  }

  @Override
  public String getSendMessage() {
    return DISBAND_TEAM;
  }
}
