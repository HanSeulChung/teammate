package com.api.backend.documents.data.repository;

import com.api.backend.documents.data.entity.Documents;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentsRepository extends MongoRepository<Documents, Long> {
  @Override
  Page<Documents> findAll(Pageable pageable);
}
