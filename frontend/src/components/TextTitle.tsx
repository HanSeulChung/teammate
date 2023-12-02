import React from "react";
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
  const titleData = localStorage.getItem('title');
  console.log("title data: ", titleData);
  const titleInput = document.getElementById('title') as HTMLInputElement | null;
  
  if (titleInput) {
    titleInput.setAttribute('value', titleData ?? '');
    console.log('title id checked');
  }  
  const titleSave = () => {
    console.log('title save');  
  
    if (titleInput) {
      const titleText = titleInput.value;
      localStorage.setItem('title', titleText);
    }
  }

  return (
    <TitleInput
      id="title"
      placeholder="Title"
      onKeyDown={(e) => {
        if (e.key === 'Enter') {
          titleSave();
        }
      }}
    />
  )
}

export default TextTitle;