import React, { useState, ChangeEvent, FormEvent } from "react";
import { useNavigate } from "react-router-dom";
import styled from "styled-components";

const StyledCommentArea = styled.div`
  position: relative;
  width: 100%;
  max-width: 600px;
  margin: 1rem auto;
  padding: 1rem;
  border: 1px solid black;
  border-radius: 10px;
`;
const CommentInput = styled.input`
  width: 10rem;
  margin: 0.5rem 0.25rem 0 0.25rem;
  padding: 0.5rem;
  background-color: white;
  color: black;
`;

const CommentButton = styled.button`
  width: 3rem;
  height: 2rem;
  border: 1px solid black;
  color: black;
  background-color: white;
  font-weight: 600;
  font-size: 10px;
`;

const ReturnButton = styled.button`
  position: absolute;
  top: 10px;
  right: 10px;
  border: 1px solid black;
  color: black;
  background-color: white;
  font-weight: 600;
  font-size: 10px;
`;

const CommentText = styled.div`
  display: flex;
  justify-content: space-between;
  margin: 8px 0;
`;

const CommentActions = styled.div`
  display: flex;
  gap: 10px;
`;

const CommentTitle = styled.h3`
  margin: 8px 0 0 0;
`;

interface CommentProps {}

const Comment: React.FC<CommentProps> = () => {
  const [comments, setComments] = useState<{ user: string; comment: string }[]>(
    [],
  );
  const [newComment, setNewComment] = useState<string>("");
  const [currentUser, setCurrentUser] = useState<string>("user");

  const handleCommentChange = (e: ChangeEvent<HTMLInputElement>) => {
    setNewComment(e.target.value);
  };

  const navigate = useNavigate();

  const handleBackToDocument = () => {
    const currentPath = window.location.pathname;
    const newPath = currentPath.substring(0, currentPath.lastIndexOf("/"));
    navigate(newPath);
  };

  const handleAddComment = (e: FormEvent) => {
    e.preventDefault();
    if (newComment.trim() === "") {
      alert("댓글을 입력해주세요.");
      return;
    }
    setComments((prevComments) => [
      ...prevComments,
      { user: currentUser, comment: newComment },
    ]);
    setNewComment("");
  };

  const handleEditComment = (index: number) => {
    const commentToEdit = comments[index];
    setNewComment(commentToEdit.comment);
    // 추가: 현재 수정 중인 댓글의 인덱스를 저장하는 상태가 필요할 수 있습니다.
  };

  const handleDeleteComment = (index: number) => {
    const updatedComments = comments.filter((_, i) => i !== index);
    setComments(updatedComments);
  };

  return (
    <>
      <StyledCommentArea>
        <ReturnButton onClick={handleBackToDocument}>
          문서로 돌아가기
        </ReturnButton>
        <CommentTitle>Comments</CommentTitle>
        <form onSubmit={handleAddComment}>
          <CommentInput
            type="text"
            placeholder="댓글을 입력해주세요"
            value={newComment}
            onChange={handleCommentChange}
          />
          <CommentButton type="submit">확인</CommentButton>
        </form>
        {comments.map((comment, index) => (
          <CommentText key={index}>
            <span>
              {comment.user} : {comment.comment}
            </span>
            <CommentActions>
              <button onClick={() => handleEditComment(index)}>수정</button>
              <button onClick={() => handleDeleteComment(index)}>삭제</button>
            </CommentActions>
          </CommentText>
        ))}
      </StyledCommentArea>
    </>
  );
};

export default Comment;
