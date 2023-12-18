package com.api.backend.team.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TeamParticipantsDeleteResponse {
  private Long teamId;
  private Long teamParticipantsId;
  private String nickName;
  private String message;
}