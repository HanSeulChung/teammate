package com.api.backend.documents.data.entity;

import com.api.backend.comment.data.entity.Comment;
import com.api.backend.global.domain.BaseEntity;
import com.api.backend.team.data.entity.Team;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "documents")
public class Documents extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long documentsId;
  private String title;
  private String content;
  private Long writerId;
  private Long modifierId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "team_id")
  private Team team;

  @OneToMany(mappedBy = "documents")
  @Builder.Default
  private List<Comment> comments = new ArrayList<>();

}
