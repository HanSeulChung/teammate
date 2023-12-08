import TextEditor from "../components/TextEditor";
import Comment from "../components/Comment";
import styled from "styled-components";

const TextEditorView = () => {
  const StyledTextEditorView = styled.section`
    display: flex;
    justify-content: center;
  `;

  return (
    <StyledTextEditorView>
      <TextEditor />
    </StyledTextEditorView>
  );
};

export default TextEditorView;
