package com.api.backend.documents.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SelectionChangeMessage {
  private String eventName; // 이벤트 유형
  private Integer index;
  private Integer length;
}
