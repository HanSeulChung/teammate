// TextTitle 컴포넌트
import React, { useState, useEffect } from "react";
import styled from "styled-components";

const TitleInput = styled.input`
  border: 1px solid black;
  background-color: white;
  color: black;
  width: 40rem;
  font-size: 16px;
  margin-bottom: 4px;
  padding: 4px;
  ::placeholder {
    color: gray;
  }
`;

interface TextTitleProps {
  titleProps: string;
  onTitleChange: (newTitle: string) => void;
}

const TextTitle: React.FC<TextTitleProps> = ({ titleProps, onTitleChange }) => {
  const [title, setTitle] = useState<string>(titleProps);

  useEffect(() => {
    setTitle(titleProps);
    const titleData = "title";
    if (titleData) {
      setTitle(titleData);
    }
  }, [titleProps]);

  const titleSave = () => {
    // localStorage.setItem("title", title);
    onTitleChange(title);
  };

  return (
    <TitleInput
      id="title"
      placeholder="Title"
      value={title}
      onChange={(e) => {
        setTitle(e.target.value);
        console.log("title: ", e.target.value);
      }}
      onKeyDown={(e) => {
        if (e.key === "Enter") {
          titleSave();
        }
      }}
    />
  );
};

export default TextTitle;
