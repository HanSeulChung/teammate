package com.api.backend.team.data.dto;

import com.api.backend.team.data.annotation.ValidFile;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TeamCreateRequest {
  @NotBlank(message = "팀 이름이 존재하지 않습니다.")
  @Schema(description = "팀 이름", example = "팀 메이트")
  private String teamName;

  @ValidFile
  private MultipartFile teamImg;

  @Min(1)
  @Schema(description = "팀 제한 인원", example = "1")
  private int memberLimit;
}
