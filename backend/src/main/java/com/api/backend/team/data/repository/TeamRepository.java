package com.api.backend.team.data.repository;

import com.api.backend.team.data.entity.Team;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TeamRepository extends JpaRepository<Team,Long> {

  boolean existsByTeamIdAndIsDelete(Long teamId, boolean isDelete);

  List<Team> findAllByRestorationDtIsNotNull();

  List<Team> findAllByIsDeleteIsTrue();

  @Transactional
  @Modifying
  @Query(
      value = "delete from team t where t.team_id in :ids",
      nativeQuery = true
  )
  void deleteAllByIdsInQuery(@Param("ids") List<Long> teamIds);
}
