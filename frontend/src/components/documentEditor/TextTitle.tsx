import React, { useState, useEffect } from "react";
import styled from "styled-components";

interface TextTitleProps {
  titleProps: string;
  onTitleChange: (newTitle: string) => void;
}

const TextTitle: React.FC<TextTitleProps> = ({ titleProps, onTitleChange }) => {
  const [title, setTitle] = useState<string>(titleProps);

  useEffect(() => {
    setTitle(titleProps);
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
        onTitleChange(e.target.value); // 변경된 타이틀을 상위 컴포넌트에 전달
      }}
      onBlur={titleSave} // 포커스가 벗어나면 저장
    />
  );
};

export default TextTitle;

const TitleInput = styled.input`
  border: 1px solid black;
  background-color: white;
  color: black;
  width: 100%;
  font-size: 16px;
  margin-bottom: 4px;
  border: 1px solid gray;
  padding: 4px;
  ::placeholder {
    color: gray;
  }
`;
