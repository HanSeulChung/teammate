import React, { useEffect, useRef, useState } from "react";
import styled from "styled-components";
import "quill/dist/quill.snow.css";
import Quill from "quill";
import TextTitle from "./TextTitle";
import axios from "axios";

const StyledTexteditor = styled.div`
  width: 41rem;
`;

const SaveButton = styled.button`
  background-color: rgb(163, 204, 163);
  color: #333333;
  border-radius: 0.5rem;
`;

const QuillStyled = styled.div`
  height: auto;
  min-height: 300px;
`;

interface QuillEditorProps {}

const CreateText: React.FC<QuillEditorProps> = () => {
  const [quill, setQuill] = useState<Quill | null>(null);
  const [title, setTitle] = useState<string>("");
  const writerEmail = "작성자_이메일@example.com"; // 작성자 이메일, 동적으로 관리해야 함
  const teamId = "팀_ID"; // 팀 ID, 동적으로 관리해야 함

  useEffect(() => {
    if (!quill) {
      const editor = new Quill("#quill-editor", {
        theme: "snow",
      });
      setQuill(editor);
    }
  }, [quill]);

  const handleSave = async () => {
    if (!quill) return;

    const content = quill.root.innerHTML;
    const requestData = {
      title: title,
      content: content,
      writerEmail: writerEmail,
    };

    try {
      const response = await axios.post(
        `/team/${teamId}/documents`,
        requestData,
      );
      console.log(response);
      if (response.status === 200) {
        console.log("문서 저장 성공:", response.data);
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
      <SaveButton className="save" onClick={handleSave}>
        Save
      </SaveButton>
    </StyledTexteditor>
  );
};

export default CreateText;
