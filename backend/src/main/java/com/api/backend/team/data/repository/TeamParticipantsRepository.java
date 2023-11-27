package com.api.backend.team.data.repository;

import com.api.backend.team.data.entity.TeamParticipants;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamParticipantsRepository extends JpaRepository<TeamParticipants,Long> {

}
