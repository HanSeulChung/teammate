package com.api.backend.schedule.data.repository;

import com.api.backend.schedule.data.enetity.TeamParticipantsSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamParticipantsScheduleRepository extends JpaRepository<TeamParticipantsSchedule, Long> {

}
