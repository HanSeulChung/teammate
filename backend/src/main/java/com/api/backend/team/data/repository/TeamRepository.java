package com.api.backend.team.data.repository;

import com.api.backend.team.data.entity.Team;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team,Long> {

  boolean existsByTeamIdAndIsDelete(Long teamId, boolean isDelete);

  List<Team> findAllByRestorationDtIsNotNull();

  @Modifying
  @Query("SELECT t.teamId FROM Team t WHERE t.isDelete = true")
  List<Long> findIdsByIsDeleteIsTrue();

  @Modifying
  @Query("DELETE FROM Team t WHERE t.teamId IN :ids")
  void deleteAllByIdIn(@Param("ids") List<Long> ids);
}
