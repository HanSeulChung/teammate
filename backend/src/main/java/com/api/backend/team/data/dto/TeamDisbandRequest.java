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
  @NotNull(message = "teamId가 비어있습니다.")
  @Schema(description = "팀 ID", example = "1", required = true)
  private Long teamId;
  @NotBlank(message = "팀 이름을 입력해주세요")
  @Schema(description = "팀 이름", example = "teammate21312", required = true)
  private String teamName;
}
