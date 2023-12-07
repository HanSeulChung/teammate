package com.api.backend.documents.service;

import com.api.backend.documents.data.entity.Documents;
import com.api.backend.documents.data.repository.DocumentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentService {
  private final DocumentsRepository documentsRepository;

  public Page<Documents> getDocsList(Pageable pageable) {
    // TODO 조회시에도 사용자에 관해서, teamId 유효성 검사 추후 추가
    Page<Documents> allDocs = documentsRepository.findAll(pageable);

    if (allDocs == null) {
      return Page.empty(pageable);
    }
    if (allDocs.getTotalElements() == 0) {
      return Page.empty(pageable);
    }
    return allDocs;
  }
}
