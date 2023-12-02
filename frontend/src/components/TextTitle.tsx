import React, { useState, useEffect } from "react";
import styled from "styled-components";

const TitleInput = styled.input`
  border: 1px solid black;
  background-color: white;
  color: black;
  width: 39rem;
  font-size: 16px;
  margin-bottom: 4px;
  padding: 4px;
  ::placeholder {
    color: gray;
  }
`;

const TextTitle: React.FC = () => {
  const [title, setTitle] = useState<string>("");
  
  useEffect(() => {
    const titleData = localStorage.getItem('title');
    if (titleData) {
      setTitle(titleData);
    }
  }, []);

  const titleSave = () => {
    console.log('title save');  
    localStorage.setItem('title', title);
  }

  return (
    <TitleInput
      id="title"
      placeholder="Title"
      value={title}
      onChange={(e) => setTitle(e.target.value)}
      onKeyDown={(e) => {
        if (e.key === 'Enter') {
          titleSave();
        }
      }}
    />
  );
}

export default TextTitle;