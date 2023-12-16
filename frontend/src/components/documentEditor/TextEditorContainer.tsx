import React from "react";
import { useParams } from "react-router-dom";
import TextEditor from "./TextEditor";
import styled from "styled-components";

interface TextEditorViewProps {}

const StyledTextEditorView = styled.section`
  display: flex;
  justify-content: center;
  height: auto;
`;

const TextEditorContainer: React.FC<TextEditorViewProps> = () => {
  const { teamId } = useParams<{ teamId: string }>();
  const { documentsId } = useParams<{ documentsId: string }>();

  if (!documentsId) {
    return <div>No DocsID provided</div>;
  }

  if (!teamId) {
    return <div>No TeamID provided</div>;
  }

  return (
    <StyledTextEditorView>
      <TextEditor teamId={teamId} documentsId={documentsId} />
    </StyledTextEditorView>
  );
};

export default TextEditorContainer;
