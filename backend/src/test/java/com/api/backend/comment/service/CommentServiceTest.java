package com.api.backend.comment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.api.backend.comment.data.dto.CommentEditRequest;
import com.api.backend.comment.data.dto.CommentInitRequest;
import com.api.backend.comment.data.entity.Comment;
import com.api.backend.comment.data.repository.CommentRepository;
import com.api.backend.documents.data.entity.Documents;
import com.api.backend.documents.data.repository.DocumentsRepository;
import java.util.Collections;
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


@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

  @Mock
  private CommentRepository commentRepository;

  @Mock
  private DocumentsRepository documentsRepository;

  @InjectMocks
  private CommentService commentService;

  private Documents createDocuments() {
    return Documents.builder()
        .documentIdx("testDocumentIdx")
        .teamId(1L)
        .build();
  }
  private Comment createComment() {
    return Comment.builder()
        .id("commentId")
        .writerId(23L)
        .content("아하 이런 회의를 했었군요.")
        .teamId(1L)
        .build();
  }

  @Test
  @DisplayName("댓글 생성 성공")
  void createComment_Success() {
    //given
    Documents documents =createDocuments();

    Comment comment = createComment();

    CommentInitRequest commentInitRequest = CommentInitRequest.builder()
        .writerId(23L)
        .content("아하 이런 회의를 했었군요.")
        .build();

    when(documentsRepository.findByDocumentIdx("testDocumentIdx")).thenReturn(Optional.of(documents));
    when(commentRepository.save(any(Comment.class))).thenReturn(comment);

    //when
    Comment savedComment = commentService.createComment(1L, "testDocumentIdx", commentInitRequest);

    //then
    assertNotNull(savedComment);
    assertEquals(23L, savedComment.getWriterId());
    assertEquals("아하 이런 회의를 했었군요.", savedComment.getContent());
    assertEquals(1, documents.getCommentIds().size());
  }


  @Test
  @DisplayName("댓글 전체 조회 성공_댓글이 없을 때")
  void getCommentList_Success_WhenCommentsNotExist() {
    //given
    Documents documents =createDocuments();

    when(documentsRepository.findByDocumentIdx("testDocumentIdx")).thenReturn(Optional.of(documents));
    //when
    Pageable pageable = PageRequest.of(0, 4, Sort.unsorted());
    Page<Comment> commentPage = commentService.getCommentList(1L, "testDocumentIdx", pageable);

    //then
    assertNotNull(commentPage);
    assertTrue(commentPage.isEmpty());
  }

  @Test
  @DisplayName("댓글 전체 조회 성공_댓글이 있을 때(1 개)")
  void getCommentList_Success_WhenCommentsExist() {
    //given
    Comment comment = createComment();
    Documents documents = Documents.builder()
        .documentIdx("testDocumentIdx")
        .teamId(1L)
        .commentIds(Collections.singletonList(comment))
        .build();

    when(documentsRepository.findByDocumentIdx("testDocumentIdx")).thenReturn(Optional.of(documents));

    //when
    Pageable pageable = PageRequest.of(0, 4, Sort.unsorted());
    Page<Comment> commentPage = commentService.getCommentList(1L, "testDocumentIdx", pageable);

    //then
    assertNotNull(commentPage);
    assertFalse(commentPage.isEmpty());
  }

  @Test
  @DisplayName("댓글 수정 성공")
  void editComment_Success() {
    //given
    Comment comment = createComment();
    Documents documents = Documents.builder()
        .documentIdx("testDocumentIdx")
        .teamId(1L)
        .commentIds(Collections.singletonList(comment))
        .build();

    CommentEditRequest commentEditRequest = CommentEditRequest.builder()
        .editorId(23L)
        .content("수정한 댓글입니다.")
        .build();

    Comment editCommentMock = Comment.builder()
        .id("commentId")
        .writerId(23L)
        .content("수정한 댓글입니다.")
        .teamId(1L)
        .build();

    when(documentsRepository.findByDocumentIdx("testDocumentIdx")).thenReturn(Optional.of(documents));
    when(commentRepository.findById("commentId")).thenReturn(Optional.of(comment));
    when(commentRepository.save(any(Comment.class))).thenReturn(editCommentMock);
    //when
    Comment editComment = commentService.editComment(1L, "testDocumentIdx", "commentId",
        commentEditRequest);

    //then
    assertNotNull(editComment);
    assertEquals("commentId", editComment.getId());
    assertEquals("수정한 댓글입니다.", editComment.getContent());
    assertEquals(1, documents.getCommentIds().size());

    // Todo : 데이터 베이스를 이용한 통합 테스트시 해야할 일
//    assertEquals("수정한 댓글입니다.", documents.getCommentIds().get(0).getContent());

  }
}