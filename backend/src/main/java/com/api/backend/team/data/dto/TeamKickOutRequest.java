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
public class TeamKickOutRequest {
  @NotNull(message = "팀ID가 존재하지 않습니다.")
  @Schema(description = "팀 ID", example = "1" , required = true)
  private Long teamId;
  // todo id를 한다고??
  @NotNull(message = "강퇴하는 살람이 존재하지 않습니다.")
  @Schema(description = "강퇴자 ID", example = "2", required = true)
  private Long userId;
  @NotBlank(message = "강퇴 사유가 존재하지 않습니다.")
  @Schema(description = "강퇴 사유", example = "너무 팀원 같지가 않다...ㅠㅠ" , required = true)
  private String kickOutReason;
}
