import React from "react";
import styled from "styled-components";
import CreateText from "../components/documentEditor/CreateText";

interface TextEditorViewProps {}

const StyledTextEditorView = styled.section`
  display: flex;
  justify-content: center;
`;

const TextEditorView: React.FC<TextEditorViewProps> = () => {
  return (
    <StyledTextEditorView>
      <CreateText />
    </StyledTextEditorView>
  );
};

export default TextEditorView;
