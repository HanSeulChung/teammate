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
  const [editingIndex, setEditingIndex] = useState<number | null>(null);
  const [editingComment, setEditingComment] = useState<string>("");

  const navigate = useNavigate();

  const handleBackToDocument = () => {
    const currentPath = window.location.pathname;
    const newPath = currentPath.substring(0, currentPath.lastIndexOf("/"));
    navigate(newPath);
  };

  const handleCommentChange = (e: ChangeEvent<HTMLInputElement>) => {
    setNewComment(e.target.value);
  };

  const handleAddComment = (e: FormEvent) => {
    e.preventDefault();
    if (newComment.trim() === "") {
      alert("댓글을 입력해주세요.");
      return;
    }
    setComments([...comments, { user: currentUser, comment: newComment }]);
    setNewComment("");
  };

  const handleEditComment = (index: number) => {
    setEditingIndex(index);
    setEditingComment(comments[index].comment);
  };

  const handleUpdateComment = (e: FormEvent, index: number) => {
    e.preventDefault();
    const updatedComments = comments.map((comment, i) =>
      i === index ? { ...comment, comment: editingComment } : comment,
    );
    setComments(updatedComments);
    setEditingIndex(null);
    setEditingComment("");
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
        <form
          onSubmit={
            editingIndex !== null
              ? (e) => handleUpdateComment(e, editingIndex)
              : handleAddComment
          }
        >
          <CommentInput
            type="text"
            placeholder="댓글을 입력해주세요"
            value={editingIndex !== null ? editingComment : newComment}
            onChange={handleCommentChange}
          />
          <CommentButton type="submit">
            {editingIndex !== null ? "확인" : "추가"}
          </CommentButton>
        </form>
        {comments.map((comment, index) => (
          <CommentText key={index}>
            {editingIndex === index ? (
              <span>
                {currentUser} : {editingComment}
              </span>
            ) : (
              <span>
                {comment.user} : {comment.comment}
              </span>
            )}
            <CommentActions>
              {editingIndex === index ? (
                <button onClick={() => setEditingIndex(null)}>취소</button>
              ) : (
                <>
                  <button onClick={() => handleEditComment(index)}>수정</button>
                  <button onClick={() => handleDeleteComment(index)}>
                    삭제
                  </button>
                </>
              )}
            </CommentActions>
          </CommentText>
        ))}
      </StyledCommentArea>
    </>
  );
};

export default Comment;
