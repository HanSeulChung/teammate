import React, { useState, ChangeEvent, useEffect } from "react";
import styled from "styled-components";
import { useParams } from "react-router-dom";
import axiosInstance from "../../axios";
import axios from "axios";

const CommentSection = styled.div`
  align-items: center;
  padding: 20px;
  max-width: 600px;
  margin: auto;
  min-height: 800px;
`;

const CommentInputContainer = styled.form`
  margin-bottom: 12px;
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
  gap: 4px;
`;

const CommentInput = styled.input`
  width: 520px;
  padding: 8px;
  border: 1px solid black;
  border-radius: 8px;
  background-color: white;
  color: black;
  font-size: 16px;
`;

const CommentUpdateInput = styled.input`
  width: 470px;
  padding: 8px;
  border: 1px solid black;
  border-radius: 8px;
  background-color: white;
  color: black;
  font-size: 16px;
  height: 24px;
`;

const CommentButton = styled.button`
  float: right;
  width: 80px;
  margin-left: 5px;
  background-color: #a3cca3;
`;

const UDbutton = styled.button`
  width: 40px;
  padding: 0;
  background-color: #a3cca3;
`;

interface CommentType {
  content: string;
  id: number;
  comment: string;
  writerId: number;
  teamId: number;
  createdDT: string;
  updatedDT: string;
}

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
  const [participantIds, setParticipantIds] = useState<number>();
  const [nicknames, setNicknames] = useState<{ [key: number]: string }>({});

  useEffect(() => {
    const fetchParticipants = async () => {
      try {
        const response = await axiosInstance.get("/member/participants", {});
        const participant = response.data.content.find(
          (item: { teamId: number }) => item.teamId === Number(teamId),
        );
        setParticipantIds(participant ? participant.teamParticipantsId : null);
      } catch (error) {
        console.error("Error fetching participants:", error);
      }
    };
    fetchParticipants();
  }, [teamId]);

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
    setEditingComment(commentsPage.content[index].comment || "");
  };

  const handleUpdateComment = async () => {
    if (editingComment.trim() && editingIndex !== null) {
      const commentToUpdate = commentsPage.content[editingIndex];
      try {
        console.warn(editingComment, commentToUpdate.writerId + "");
        const response = await axiosInstance.put(
          `http://118.67.128.124:8080/team/${teamId}/documents/${documentsId}/comments/${commentToUpdate.id}`,
          { content: editingComment, editorId: commentToUpdate.writerId + "" },
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
    const confirmDelete = window.confirm("삭제하시겠습니까?");

    if (confirmDelete) {
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
    }
  };
  const handleAddComment = async () => {
    if (!newComment.trim()) return;
    try {
      const response = await axiosInstance.post(
        `http://118.67.128.124:8080/team/${teamId}/documents/${documentsId}/comments`,
        {
          content: newComment,
          writerId: participantIds,
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

  const getTeamParticipantNickname = async (
    teamParticipantsId: number,
    teamId: number,
  ) => {
    try {
      const response = await axiosInstance.get(
        "http://118.67.128.124:8080/member/participants",
      );

      const participants = response.data.content;

      const nicknames = response.data.content
        .filter((data: { teamId: number }) => data.teamId === teamId)
        .filter(
          (data: { teamParticipantsId: number | undefined }) =>
            data.teamParticipantsId === participantIds,
        )
        .map((data: { teamNickName: any }) => data.teamNickName);

      return nicknames;
    } catch (error) {
      console.error("Error fetching team participants:", error);
      return "데이터를 가져오는데 실패했습니다.";
    }
  };

  useEffect(() => {
    const fetchNicknames = async () => {
      const newNicknames: { [key: string]: string } = {};
      for (const comment of commentsPage.content) {
        const nickname = await getTeamParticipantNickname(
          comment.writerId,
          Number(teamId),
        );
        newNicknames[comment.writerId.toString()] = nickname;
      }
      setNicknames(newNicknames);
    };

    if (commentsPage.content.length > 0) {
      fetchNicknames();
    }
  }, [commentsPage, teamId]);

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
          placeholder="댓글을 작성해 주세요."
        />
        <CommentButton type="submit">확인</CommentButton>
      </CommentInputContainer>

      <CommentList>
        {commentsPage.content.length > 0 ? (
          commentsPage.content.map((comment, index) => {
            const isAuthor = comment.writerId == participantIds;

            return (
              <CommentListItem key={comment.id}>
                {editingIndex === index ? (
                  <>
                    <CommentUpdateInput
                      type="text"
                      value={editingComment}
                      onChange={handleCommentChange}
                    />
                    <CommentActions>
                      <UDbutton onClick={handleUpdateComment}>확인</UDbutton>
                      <UDbutton onClick={handleCancelEdit}>취소</UDbutton>
                    </CommentActions>
                  </>
                ) : (
                  <>
                    <CommentContent>
                      <strong>{nicknames[comment.writerId]}</strong>:{" "}
                      {comment.content}
                    </CommentContent>
                    {isAuthor && (
                      <CommentActions>
                        <UDbutton onClick={() => handleEdit(index)}>
                          수정
                        </UDbutton>
                        <UDbutton onClick={() => handleDelete(comment.id)}>
                          삭제
                        </UDbutton>
                      </CommentActions>
                    )}
                  </>
                )}
              </CommentListItem>
            );
          })
        ) : (
          <div>댓글이 없습니다.</div>
        )}
      </CommentList>
    </CommentSection>
  );
};

export default Comment;
