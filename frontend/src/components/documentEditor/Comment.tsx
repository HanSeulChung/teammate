import React, { useState, ChangeEvent, useEffect } from "react";
import styled from "styled-components";
import { useParams } from "react-router-dom";
import axiosInstance from "../../axios";

// 스타일 컴포넌트 정의
const CommentSection = styled.div`
  align-items: center;
  padding: 20px;
  max-width: 600px;
  margin: auto;
  min-height: 800px;
`;

const CommentInputContainer = styled.form`
  display: flex;
`;

const CommentList = styled.ul`
  list-style-type: none;
  padding: 0;
`;

const CommentListItem = styled.li`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  border-bottom: 1px solid #ccc;
  padding-bottom: 10px;
`;

const CommentContent = styled.span`
  flex-grow: 1;
`;

const CommentActions = styled.div`
  display: flex;
  gap: 10px;
`;

const CommentInput = styled.input`
  width: 520px;
  padding: 8px;
  border-radius: 8px;
  background-color: white;
  color: black;
  font-size: 16px;
`;

const CommentButton = styled.button`
  float: right;
  width: 80px;
  margin-right: 5px;
`;

// 댓글 타입 정의
interface CommentType {
  id: number;
  comment: string;
  writerId: number;
  teamId: number;
  createdDT: string;
  updatedDT: string;
}

// 페이지 형식의 댓글 데이터
interface CommentsPage {
  content: CommentType[];
}

const Comment: React.FC = () => {
  const [commentsPage, setCommentsPage] = useState<CommentsPage>({
    content: [],
  });
  const [editingIndex, setEditingIndex] = useState<number | null>(null);
  const [editingComment, setEditingComment] = useState("");
  const [newComment, setNewComment] = useState("");
  const { teamId, documentsId } = useParams<{
    teamId: string;
    documentsId: string;
  }>();

  useEffect(() => {
    const fetchComments = async () => {
      try {
        const response = await axiosInstance.get<{ content: CommentType[] }>(
          `http://118.67.128.124:8080/team/${teamId}/documents/${documentsId}/comments`,
        );
        setCommentsPage(response.data);
      } catch (error) {
        console.error("댓글 가져오기 실패:", error);
      }
    };

    fetchComments();
  }, [teamId, documentsId]);

  const handleCommentChange = (e: ChangeEvent<HTMLInputElement>) => {
    setEditingComment(e.target.value);
  };

  const handleEdit = (index: number) => {
    setEditingIndex(index);
    setEditingComment(commentsPage.content[index].comment);
  };

  const handleUpdateComment = async () => {
    if (editingComment.trim() && editingIndex !== null) {
      const commentToUpdate = commentsPage.content[editingIndex];
      try {
        const response = await axiosInstance.put(
          `http://118.67.128.124:8080/team/${teamId}/documents/${documentsId}/comments/${commentToUpdate.id}`,
          { comment: editingComment, editorId: commentToUpdate.writerId },
        );
        const updatedComments = commentsPage.content.map((comment, index) =>
          index === editingIndex ? response.data : comment,
        );
        setCommentsPage({ ...commentsPage, content: updatedComments });
        setEditingIndex(null);
        setEditingComment("");
      } catch (error) {
        console.error("댓글 수정 실패:", error);
      }
    }
  };

  const handleCancelEdit = () => {
    setEditingIndex(null);
    setEditingComment("");
  };

  const handleDelete = async (commentId: number) => {
    try {
      await axiosInstance.delete(
        `http://118.67.128.124:8080/team/${teamId}/documents/${documentsId}/comments/${commentId}`,
      );
      const updatedComments = commentsPage.content.filter(
        (comment) => comment.id !== commentId,
      );
      setCommentsPage({ ...commentsPage, content: updatedComments });
    } catch (error) {
      console.error("댓글 삭제 실패:", error);
    }
  };

  const handleAddComment = async () => {
    if (!newComment.trim()) return;
    try {
      const response = await axiosInstance.post(
        `http://118.67.128.124:8080/team/${teamId}/documents/${documentsId}/comments`,
        {
          comment: newComment,
          writerId: JSON.parse(localStorage.getItem("user") ?? "").id,
        },
      );
      setCommentsPage({
        ...commentsPage,
        content: [...commentsPage.content, response.data],
      });
      setNewComment("");
    } catch (error) {
      console.error("댓글 추가 실패:", error);
    }
  };

  const handleNewCommentChange = (e: ChangeEvent<HTMLInputElement>) => {
    setNewComment(e.target.value);
  };

  return (
    <CommentSection>
      <CommentInputContainer
        onSubmit={(e) => {
          e.preventDefault();
          handleAddComment();
        }}
      >
        <CommentInput
          type="text"
          value={newComment}
          onChange={handleNewCommentChange}
          placeholder="Write a comment..."
        />
        <CommentButton type="submit">확인</CommentButton>
      </CommentInputContainer>

      <CommentList>
        {commentsPage.content.length > 0 ? (
          commentsPage.content.map((comment, index) => (
            <CommentListItem key={comment.id}>
              {editingIndex === index ? (
                <>
                  <CommentInput
                    type="text"
                    value={editingComment}
                    onChange={handleCommentChange}
                  />
                  <CommentActions>
                    <CommentButton onClick={handleUpdateComment}>
                      확인
                    </CommentButton>
                    <CommentButton onClick={handleCancelEdit}>
                      취소
                    </CommentButton>
                  </CommentActions>
                </>
              ) : (
                <>
                  <CommentContent>
                    <strong>{comment.writerId}</strong>: {comment.comment}
                  </CommentContent>
                  <CommentActions>
                    <CommentButton onClick={() => handleEdit(index)}>
                      수정
                    </CommentButton>
                    <CommentButton onClick={() => handleDelete(comment.id)}>
                      삭제
                    </CommentButton>
                  </CommentActions>
                </>
              )}
            </CommentListItem>
          ))
        ) : (
          <div>댓글이 없습니다.</div>
        )}
      </CommentList>
    </CommentSection>
  );
};

export default Comment;
