package com.api.backend.documents.data.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DocumentInitRequest {
  @NotNull
  private String title;

  @NotNull
  private String content;

  @NotNull
  private String writerEmail;

}
