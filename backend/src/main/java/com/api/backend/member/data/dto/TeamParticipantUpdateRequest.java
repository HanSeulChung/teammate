package com.api.backend.member.data.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamParticipantUpdateRequest {

  @NotNull(message = "팀 참가자 ID가 비어있습니다.")
  private Long teamParticipantsId;
  @NotNull(message = "팀 참가자 닉네임이 비어있습니다.")
  private String teamNickName;
  @NotNull(message = "팀 참가자 프로필이 비어있습니다.")
  private String participantsProfileUrl;
}
