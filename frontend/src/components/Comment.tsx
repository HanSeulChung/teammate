import React, { useState, ChangeEvent } from "react";
import styled from "styled-components";

const StyledCommentArea = styled.div`
  width: 15rem;
  color: black;
  border: 1px solid black;
  margin: 1rem;
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

const CommentText = styled.div`
  display: flex;
  margin-left: 8px;
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
  const [currentUser, setCurrentUser] = useState<string>("user"); // Assuming you have a state for the current user

  const handleCommentChange = (e: ChangeEvent<HTMLInputElement>) => {
    setNewComment(e.target.value);
  };

  const handleAddComment = () => {
    setComments((prevComments) => [
      ...prevComments,
      { user: currentUser, comment: newComment },
    ]);
    setNewComment("");
  };

  return (
    <StyledCommentArea>
      <CommentTitle>Comments</CommentTitle>

      <CommentInput
        type="text"
        placeholder="댓글을 입력해주세요"
        value={newComment}
        onChange={handleCommentChange}
      />
      <CommentButton onClick={handleAddComment}>확인</CommentButton>
      {comments.map((comment, index) => (
        <CommentText key={index}>
          {comment.user} : {comment.comment}
        </CommentText>
      ))}
    </StyledCommentArea>
  );
};

export default Comment;
