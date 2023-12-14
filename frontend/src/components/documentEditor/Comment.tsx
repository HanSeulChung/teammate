import React, { useState, ChangeEvent } from "react";
import styled from "styled-components";

// Styled components
const CommentSection = styled.div`
  align-item: center;
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

// Component
const Comment: React.FC = () => {
  const [comments, setComments] = useState<{ user: string; content: string }[]>(
    [],
  );
  const [editingIndex, setEditingIndex] = useState<number | null>(null);
  const [editingComment, setEditingComment] = useState("");
  const [newComment, setNewComment] = useState("");

  const handleCommentChange = (e: ChangeEvent<HTMLInputElement>) => {
    setEditingComment(e.target.value);
  };

  const handleEdit = (index: number) => {
    setEditingIndex(index);
    setEditingComment(comments[index].content);
  };

  const handleUpdateComment = () => {
    if (editingComment.trim() && editingIndex !== null) {
      const updatedComments = comments.map((comment, i) =>
        i === editingIndex
          ? { ...comment, content: editingComment.trim() }
          : comment,
      );
      setComments(updatedComments);
      setEditingIndex(null);
      setEditingComment("");
    }
  };

  const handleCancelEdit = () => {
    setEditingIndex(null);
    setEditingComment("");
  };

  const handleDelete = (index: number) => {
    const updatedComments = comments.filter((_, i) => i !== index);
    setComments(updatedComments);
  };

  const handleAddComment = () => {
    if (newComment.trim()) {
      const updatedComments = [
        ...comments,
        { user: "User", content: newComment.trim() },
      ];
      setComments(updatedComments);
      setNewComment("");
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
        {comments.map((comment, index) => (
          <CommentListItem key={index}>
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
                  <CommentButton onClick={handleCancelEdit}>취소</CommentButton>
                </CommentActions>
              </>
            ) : (
              <>
                <CommentContent>
                  <strong>{comment.user}</strong>: {comment.content}
                </CommentContent>
                <CommentActions>
                  <CommentButton onClick={() => handleEdit(index)}>
                    수정
                  </CommentButton>
                  <CommentButton onClick={() => handleDelete(index)}>
                    삭제
                  </CommentButton>
                </CommentActions>
              </>
            )}
          </CommentListItem>
        ))}
      </CommentList>
    </CommentSection>
  );
};

export default Comment;
