package com.api.backend.documents.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SelectionChangeMessage {
  private String eventName; // 이벤트 유형
  private Integer index;
  private Integer length;
}
