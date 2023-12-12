package com.api.backend.team.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TeamDisbandRequest {
  @NotNull(message = "teamId는 비어있는 값입니다.")
  @Schema(description = "팀 ID", example = "1")
  private Long teamId;
  @NotBlank(message = "비밀번호를 입력해주세요")
  @Schema(description = "팀장 비밀번호", example = "teammate21312")
  private String password;
}
