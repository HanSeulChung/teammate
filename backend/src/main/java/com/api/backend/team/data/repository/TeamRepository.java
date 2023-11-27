package com.api.backend.team.data.repository;

import com.api.backend.team.data.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team,Long> {

}
