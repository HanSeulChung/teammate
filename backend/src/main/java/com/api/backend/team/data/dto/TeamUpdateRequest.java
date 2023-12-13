package com.api.backend.team.data.dto;

import com.api.backend.team.data.annotation.ValidFile;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamUpdateRequest {
  @NotBlank(message = "팀 id가 비어 있습니다.")
  @Schema(description = "팀 ID", example = "1", required = true)
  private Long teamId;
  @NotBlank(message = "팀 이름이 비어 있습니다.")
  @Schema(description = "팀 이름", example = "수정되는 팀 이름")
  private String teamName;
  @ValidFile
  @Schema(description = "이미지", example = "수정되는 팀 이미지")
  private MultipartFile profileImg;
}
