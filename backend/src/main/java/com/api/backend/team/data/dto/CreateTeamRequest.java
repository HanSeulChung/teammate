package com.api.backend.team.data.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateTeamRequest {
  @NotBlank(message = "팀 이름이 존재하지 않습니다.")
  private String teamName;
  @NotBlank(message = "팀 이미지가 존재하지 않습니다.")
  private String teamImg;
  @NotBlank(message = "팀 제한이 설정되어 있지 않습니다.")
  private int memberLimit;
}
