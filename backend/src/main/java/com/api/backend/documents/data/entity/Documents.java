package com.api.backend.documents.data.entity;

import com.api.backend.comment.data.entity.Comment;
import com.api.backend.global.domain.BaseEntity;
import com.api.backend.team.data.entity.Team;
import com.vladmihalcea.hibernate.type.json.JsonType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@TypeDef(name = "json", typeClass = JsonType.class)
public class Documents extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long documentsId;
  private String title;
  private String content;
  private Long writerId;
  private Long modifierId;
  @Type(type = "json")
  @Column(columnDefinition = "longtext")
  private HashMap<Long, String> documentsParticipantMap = new HashMap<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "teamId")
  private Team team;

  @OneToMany(mappedBy = "documents")
  private List<Comment> comments = new ArrayList<>();

}
