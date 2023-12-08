package com.api.backend.comment.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.api.backend.comment.data.dto.CommentRequest;
import com.api.backend.comment.data.entity.Comment;
import com.api.backend.comment.data.repository.CommentRepository;
import com.api.backend.documents.data.entity.Documents;
import com.api.backend.documents.data.repository.DocumentsRepository;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;


@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

  @Mock
  private CommentRepository commentRepository;

  @Mock
  private DocumentsRepository documentsRepository;

  @InjectMocks
  private CommentService commentService;

  @Mock
  private MongoTemplate mongoTemplate;


  @Test
  @DisplayName("댓글 생성 성공")
  void createComment_Success() {
    //given
    Documents documents = Documents.builder()
        .documentIdx("testDocumentIdx")
        .teamId(1L)
        .build();

    Comment comment = Comment.builder()
        .id("commentId")
        .writerId(23L)
        .content("아하 이런 회의를 했었군요.")
        .teamId(1L)
        .build();

    CommentRequest commentRequest = CommentRequest.builder()
        .writerId(23L)
        .content("아하 이런 회의를 했었군요.")
        .build();

    when(documentsRepository.findByDocumentIdx("testDocumentIdx")).thenReturn(Optional.of(documents));
    when(commentRepository.save(any(Comment.class))).thenReturn(comment);

    //when
    Comment savedComment = commentService.createComment(1L, "testDocumentIdx", commentRequest);

    //then
    assertNotNull(savedComment);
    assertEquals(23L, savedComment.getWriterId());
    assertEquals("아하 이런 회의를 했었군요.", savedComment.getContent());
    assertEquals(1, documents.getCommentIds().size());
  }
}