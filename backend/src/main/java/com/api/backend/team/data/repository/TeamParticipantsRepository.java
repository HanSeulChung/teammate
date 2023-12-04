package com.api.backend.team.data.repository;

import com.api.backend.team.data.entity.TeamParticipants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamParticipantsRepository extends JpaRepository<TeamParticipants,Long> {


  boolean existsByIdAndMember_MemberId(Long teamId,Long userId);
}
