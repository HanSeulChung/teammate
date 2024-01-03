package com.api.backend.documents.data.repository;

import com.api.backend.documents.data.entity.Documents;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DocumentsRepository extends MongoRepository<Documents, String> {
  @Override
  Page<Documents> findAll(Pageable pageable);


  @Transactional
  void deleteById(String documentId);

  List<Documents> findAllByTeamId(Long teamId);

  List<Documents> findAllByTeamIdAndCreatedDtGreaterThanEqual(Long teamId, LocalDateTime startDt);

  List<Documents> findAllByTeamIdAndCreatedDtLessThanEqual(Long teamId, LocalDateTime endDt);

  List<Documents> findAllByTeamIdAndCreatedDtBetween(Long teamId, LocalDateTime startDt, LocalDateTime endDt);
}
