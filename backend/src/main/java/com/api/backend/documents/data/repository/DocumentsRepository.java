package com.api.backend.documents.data.repository;

import com.api.backend.documents.data.entity.Documents;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DocumentsRepository extends MongoRepository<Documents, String> {
  @Override
  Page<Documents> findAll(Pageable pageable);

  Optional<Documents> findByDocumentIdx(String documentIdx);

  @Transactional
  void deleteByDocumentIdx(String documentIdx);

}
