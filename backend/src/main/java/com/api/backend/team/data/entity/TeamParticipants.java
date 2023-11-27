package com.api.backend.team.data.entity;

import com.api.backend.global.domain.BaseEntity;
import com.api.backend.member.data.entity.Member;
import com.api.backend.team.data.type.TeamRole;
import com.api.backend.team.data.type.converter.TeamRoleConverter;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class TeamParticipants extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long teamParticipantsId;

  @Convert(converter = TeamRoleConverter.class)
  private TeamRole teamRole;
  private String participantsProfileUrl;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "memberId")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "teamId")
  private Team team;
}
