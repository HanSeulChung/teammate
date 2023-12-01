package com.api.backend.documents.data.repository;

import com.api.backend.documents.data.entity.Documents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentsRepository extends JpaRepository<Documents,Long> {

}
