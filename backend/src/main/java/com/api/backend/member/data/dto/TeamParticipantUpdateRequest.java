package com.api.backend.member.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TeamParticipantUpdateRequest {

  @NotNull(message = "팀 참가자 ID가 비어있습니다.")
  @Schema(description = "팀 참가자 ID", example = "1", required = true)
  private Long teamParticipantsId;
  @NotBlank(message = "팀 닉네임이 null 입니다.")
  @Schema(description = "팀 참가자 닉네임", example = "팀메이트")
  private String teamNickName;
  @Schema(description = "팀 참가자 이미지", example = "이미지.png")
  @NotNull(message = "팀 참가자 이미지가 null 입니다.")
  private MultipartFile participantImg;
}
