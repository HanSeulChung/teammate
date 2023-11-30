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
  margin-bottom: 0.5rem;
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
  font-size: 11px
`

const CommentText = styled.div`
  display:flex;
  margin-left: 8px;
`

interface CommentProps {}

const Comment: React.FC<CommentProps> = () => {
  const [comments, setComments] = useState<string[]>([]);
  const [newComment, setNewComment] = useState<string>("");

  const handleCommentChange = (e: ChangeEvent<HTMLInputElement>) => {
    setNewComment(e.target.value);
  };

  const handleAddComment = () => {
    setComments((prevComments) => [...prevComments, newComment]);
    setNewComment("");
  };

  console.log("comment");

  return (
    <StyledCommentArea>
      <h3>Comments</h3>
      
      <CommentInput
        type="text"
        placeholder="댓글을 입력해주세요"
        value={newComment}
        onChange={handleCommentChange}
      />
      <CommentButton onClick={handleAddComment}>확인</CommentButton>
      {comments.map((comment, index) => (
        <CommentText key={index}>{comment}</CommentText>
      ))}
    </StyledCommentArea>
  );
};

export default Comment;