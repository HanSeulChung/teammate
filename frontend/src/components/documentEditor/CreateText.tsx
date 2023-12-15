import React, { useEffect, useRef, useState } from "react";
import styled from "styled-components";
import "quill/dist/quill.snow.css";
import Quill from "quill";
import TextTitle from "./TextTitle";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { accessTokenState } from "../../state/authState";
import { useRecoilValue } from "recoil";

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

const QuillStyled = styled.div`
  height: auto;
  min-height: 300px;
`;

interface QuillEditorProps {}

const CreateText: React.FC<QuillEditorProps> = () => {
  const [quill, setQuill] = useState<Quill | null>(null);
  const [title, setTitle] = useState<string>("");
  const writerEmail = "default@mail.com";
  const teamId = "DefaultTeam_ID";
  const navigate = useNavigate();
  const accessToken = useRecoilValue(accessTokenState);

  useEffect(() => {
    const editor = new Quill("#quill-editor", {
      theme: "snow",
    });
    setQuill(editor);
  }, []);

  const handleSave = async () => {
    if (!quill) return;
    if (title === "") {
      alert("제목을 입력해 주세요.");
      return;
    }
    const content = quill.root.innerHTML;
    if (content === "<p><br></p>") {
      alert("내용을 입력해 주세요.");
      return;
    }

    const requestData = {
      title: title,
      content: content,
      writerEmail: writerEmail,
    };

    try {
      const response = await axios.post(
        `http://118.67.128.124:8080/team/${teamId}/documents`,
        requestData,
        {
          headers: {
            // 여기에 필요한 토큰을 포함시킵니다.
            Authorization: `Bearer ${accessToken}`,
          },
        },
      );

      if (response.status === 200) {
        console.log("문서 저장 성공:", response.data);
        navigate(`/team/${teamId}/documentsList`); // 저장 후 이동할 페이지
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
      <QuillStyled id="quill-editor" />
      <SaveButton onClick={handleSave}>저장</SaveButton>
    </StyledTexteditor>
  );
};

export default CreateText;
