package com.api.backend.team.data.repository;

import com.api.backend.team.data.entity.Team;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team,Long> {

  boolean existsByTeamIdAndIsDelete(Long teamId, boolean isDelete);

  List<Team> findByRestorationDtIsNotNull();
}
