package com.api.backend.schedule.data.repository;

import com.api.backend.schedule.data.entity.TeamParticipantsSchedule;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TeamParticipantsScheduleRepository extends JpaRepository<TeamParticipantsSchedule, Long> {
  List<TeamParticipantsSchedule> findAllBySimpleSchedule_SimpleScheduleId (Long scheduleId);

  List<TeamParticipantsSchedule> findAllByRepeatSchedule_RepeatScheduleId (Long scheduleId);

  @Transactional
  @Query(
      value = "select t.team_participants_schedule_id from team_participants_schedule t where t.team_participants_id = :id",
      nativeQuery = true
  )
  List<Long> findAllIdByQuery(@Param("id") Long teamParticipantId);

  @Transactional
  @Modifying
  @Query(
      value = "delete from team_participants_schedule t where t.team_participants_schedule_id in :ids",
      nativeQuery = true
  )
  void deleteAllByIdInQuery(@Param("ids") List<Long> participantScheduleIds);
}
