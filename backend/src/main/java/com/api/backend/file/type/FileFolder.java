package com.api.backend.file.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileFolder {
  // 텍스트 외에 이미지 파일까지 업로드 시 사용 대비
  DOCUMENT("문서"),
  TEAM("팀"),
  PARTICIPANT("참가자");

  private final String description;
}
