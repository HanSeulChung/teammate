import React, { useEffect, useRef, useState } from "react";
import styled from "styled-components";
import "quill/dist/quill.snow.css";
import ReactQuill from "react-quill";
import TextTitle from "./TextTitle";
import { useNavigate, useParams } from "react-router-dom";
import axiosInstance from "../../axios";
import "./ReactQuill.css";

interface QuillEditorProps {}

const CreateText: React.FC<QuillEditorProps> = () => {
  const [title, setTitle] = useState<string>("");
  const [content, setContent] = useState<string>(""); // 편집 내용을 저장하는 상태
  const { teamId } = useParams<{ teamId: string }>();
  const navigate = useNavigate();

  const handleSave = async () => {
    if (title === "") {
      alert("제목을 입력해 주세요.");
      return;
    }
    if (content === "<p><br></p>") {
      alert("내용을 입력해 주세요.");
      return;
    }

    const requestData = {
      title: title,
      content: content
        .replace(/<p>/g, "")
        .replace(/<\/p>/g, "\n")
        .replace(/<br>/g, ""),
      writerEmail: JSON.parse(sessionStorage.getItem("user") ?? "").id,
    };

    //console.log(requestData);

    //console.log("teamid : ", teamId);

    try {
      const response = await axiosInstance.post(
        `/team/${teamId}/documents`,
        requestData,
      );

      if (response.status === 200) {
        //console.log("문서 저장 성공:", response.data);
        navigate(`/team/${teamId}/documentsList`);
      } else {
        console.error("문서 저장 실패:", response.status);
      }
    } catch (error) {
      console.error("문서 저장 중 에러 발생:", error);
    }
  };

  return (
    <StyledTexteditor className="texteditor">
      <TextTitle titleProps={title} onTitleChange={setTitle} />
      <ReactQuill value={content} onChange={setContent} />
      <SaveButton onClick={handleSave}>저장</SaveButton>
    </StyledTexteditor>
  );
};
export default CreateText;

const StyledTexteditor = styled.div`
  displey: flex;
  width: 41rem;
  text-align: center;
`;

const SaveButton = styled.button`
  background-color: rgb(163, 204, 163);
  color: #333333;
  border-radius: 0.5rem;
  margin: 12px;
`;
