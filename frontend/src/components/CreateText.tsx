import React, { useEffect, useRef, useState } from "react";
import styled from "styled-components";
import "quill/dist/quill.snow.css";
import Quill from "quill";
import TextTitle from "./TextTitle";

const StyledTexteditor = styled.div`
  width: 41rem;
`;

const SaveButton = styled.button`
  background-color: rgb(163, 204, 163);
  color: #333333;
  border-radius: 0.5rem;
`;

interface QuillEditorProps {}

const CreateText: React.FC<QuillEditorProps> = () => {
  const [quill, setQuill] = useState<Quill | null>(null);
  const [title, setTitle] = useState<string>("");

  useEffect(() => {
    const initializeQuill = () => {
      if (quill) {
        return;
      }

      const editor = new Quill("#quill-editor", {
        theme: "snow",
      });

      setQuill(editor);
    };

    initializeQuill();
  }, [quill]);

  const handleSave = async () => {
    if (!quill) {
      return;
    }

    const content = quill.root.innerHTML; // Quill 에디터에서 내용 가져오기
    console.log(content);

    const requestData = {
      title: title, // Updated to use the 'title' state
      teamId: "팀_ID",
      writer: "작성자_이름",
      content: content,
    };

    console.log(requestData);

    try {
      const response = await fetch("/team/팀_ID/documents", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(requestData),
      });

      if (response.ok) {
        console.log("save success");
      } else {
        console.error("save fail");
      }
    } catch (error) {
      console.error("error :", error);
    }
  };

  const toolbar = document.getElementsByClassName("ql-toolbar");
  if (toolbar.length > 1) {
    toolbar[0].parentNode.removeChild(toolbar[0]);
  }

  return (
    <StyledTexteditor className="texteditor">
      <TextTitle
        titleProps={title}
        onTitleChange={(newTitle) => setTitle(newTitle)}
      />
      <div id="quill-editor" />
      <SaveButton
        className="save"
        type="button"
        onClick={() => {
          handleSave();
        }}
      >
        Save
      </SaveButton>
    </StyledTexteditor>
  );
};

export default CreateText;
