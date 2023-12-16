import axios from "axios";
import React, { useState, ChangeEvent, useEffect } from "react";
import styled from "styled-components";
import { useParams } from "react-router-dom";
import { useRecoilValue } from "recoil";
import { accessTokenState } from "../../state/authState";

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
interface CommentType {
  id: number;
  comment: string;
  writerId: number;
  createdDT: string;
  updatedDT: string;
}

const Comment: React.FC = () => {
  const [comments, setComments] = useState<CommentType[]>([]);
  const [editingIndex, setEditingIndex] = useState<number | null>(null);
  const [editingComment, setEditingComment] = useState("");
  const [newComment, setNewComment] = useState("");
  const accessToken = useRecoilValue(accessTokenState);

  const { teamId, documentsId } = useParams<{
    teamId: string;
    documentsId: string;
  }>();

  useEffect(() => {
    const fetchComments = async () => {
      try {
        const response = await axios.get(
          `http://118.67.128.124:8080/team/${teamId}/documents/${documentsId}/comments`,
          { headers: { Authorization: `Bearer ${accessToken}` } },
        );
        console.log("data?", response.data);
        setComments(response.data);
      } catch (error) {
        console.error("댓글 가져오기 실패:", error);
      }
    };

    fetchComments();
  }, [teamId, documentsId, accessToken]);

  const handleCommentChange = (e: ChangeEvent<HTMLInputElement>) => {
    setEditingComment(e.target.value);
  };

  const handleEdit = (index: number) => {
    setEditingIndex(index);
    setEditingComment(comments[index].comment);
  };

  const handleUpdateComment = async () => {
    if (editingComment.trim() && editingIndex !== null) {
      const commentToUpdate = comments[editingIndex];
      try {
        const response = await axios.put(
          `http://118.67.128.124:8080/team/${teamId}/documents/${documentsId}/comments/${commentToUpdate.id}`,
          { comment: editingComment, editorId: commentToUpdate.writerId },
          { headers: { Authorization: `Bearer ${accessToken}` } },
        );
        const updatedComments = comments.map((comment, index) =>
          index === editingIndex ? response.data : comment,
        );
        setComments(updatedComments);
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
      await axios.delete(
        `http://118.67.128.124:8080/team/${teamId}/documents/${documentsId}/comments/${commentId}`,
        { headers: { Authorization: `Bearer ${accessToken}` } },
      );
      const updatedComments = comments.filter(
        (comment) => comment.id !== commentId,
      );
      setComments(updatedComments);
    } catch (error) {
      console.error("댓글 삭제 실패:", error);
    }
  };

  const handleAddComment = async () => {
    if (!newComment.trim()) return;
    try {
      const response = await axios.post(
        `http://118.67.128.124:8080/team/${teamId}/documents/${documentsId}/comments`,
        { comment: newComment, writerId: 1 }, // writerId 교체하기!!!!!!!
        { headers: { Authorization: `Bearer ${accessToken}` } },
      );
      setComments([...comments, response.data]);
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
        {comments.length > 0 ? (
          comments.map((comment, index) => (
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
