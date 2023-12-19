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
public class DeltaMessage {
  private String eventName; // 이벤트 유형
  private Object deltaValue; // Quill에서 받은 변경된 delta 값
}
