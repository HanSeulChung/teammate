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
public class TotalMessage {
  private String eventName; // 이벤트 유형
  private String title; // Quill에서 받은 title 값
  private String content; // Quill에서 받은 전체 content 값
  private String memberEmail;
  private Long participantsId;
  private String documentId;
}

