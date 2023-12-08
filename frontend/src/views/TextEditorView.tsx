import TextEditor from "../components/TextEditor";
import Comment from "../components/Comment";
import styled from "styled-components";

const TextEditorView = () => {
  const StyledTextEditorView = styled.section`
    display: flex;
  `

  return(
    <StyledTextEditorView>
      <TextEditor />
      <Comment />
    </StyledTextEditorView>
  )
}

export default TextEditorView;