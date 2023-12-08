package com.api.backend.team.data.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeamUpdateRequest {
  @NotNull(message = "팀 id가 비어 있습니다.")
  private Long teamId;
  @NotNull(message = "팀 id가 비어 있습니다.")
  private String teamName;
  private MultipartFile profileImg;
}
