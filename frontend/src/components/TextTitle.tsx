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
  titleProps: string; // Corrected prop name
}

const TextTitle: React.FC<TextTitleProps> = ({ titleProps }) => {
  const [title, setTitle] = useState<string>("");
  
  useEffect(() => {
    // Set the titleProps initially when the component mounts
    setTitle(titleProps);

    const titleData = localStorage.getItem("title");
    if (titleData) {
      setTitle(titleData);
    }
  }, [titleProps]);

  const titleSave = () => {
    localStorage.setItem("title", title);
  };

  return (
    <TitleInput
      id="title"
      placeholder="Title"
      value={title}
      onChange={(e) => setTitle(e.target.value)}
      onKeyDown={(e) => {
        if (e.key === "Enter") {
          titleSave();
        }
      }}
    />
  );
};

export default TextTitle;
