import React, {
  useEffect,
  useCallback,
  useRef,
  useState,
  FormEvent,
} from "react";
import {
  Editor,
  EditorState,
  RichUtils,
  DraftEditorCommand,
  convertToRaw,
  convertFromRaw,
  ContentState,
  EditorChangeType,
} from "draft-js";
import "draft-js/dist/Draft.css";
import styled from "styled-components";
import "./TextEditor.css";
import TextTitle from "./TextTitle";
import * as StompJs from "@stomp/stompjs";
import testText from "../assets/test-text.json";

const StyledTexteditor = styled.div`
  width: 41rem;
`;

const StyledButton = styled.button`
  border: 1px solid white;
  background-color: rgb(163, 204, 163);
  padding: 0.5rem 1.5rem;
  color: #333333;
`;

const SaveButton = styled.button`
  background-color: rgb(163, 204, 163);
  color: #333333;
  border-radius: 0.5rem;
`;

const ButtonContainer = styled.div`
  width: 41rem;
`;

const TextEditor: React.FC = () => {
  const [currentText, setCurrentText] = React.useState<string>("");
  const [title, setTitle] = React.useState<string>("");

  const docsIdx = "907817ea-d525-417d-8f7f-f24ef4a6a7d4";

  const data = testText;
  const initialState = data
    ? EditorState.createWithContent(convertFromRaw(data))
    : EditorState.createEmpty();
  const [editorState, setEditorState] =
    React.useState<EditorState>(initialState);

  const handleSave = useCallback(() => {
    const data = JSON.stringify(convertToRaw(editorState.getCurrentContent()));
  }, [editorState]);

  const handleChange = (newEditorState: EditorState) => {
    const contentState = newEditorState.getCurrentContent();
    const text = contentState.getPlainText();
    setCurrentText(text);

    // 현재 선택 상태 가져오기
    const selectionState = newEditorState.getSelection();

    // 선택에 관한 정보 추출 (예: anchorOffset, focusOffset, isBackward 등)
    console.log("커서 위치:", selectionState.toJS());
  };

  const handleKeyCommand = (command: DraftEditorCommand) => {
    const newState = RichUtils.handleKeyCommand(editorState, command);
    if (newState) {
      setEditorState(newState);
      return "handled";
    }
    return "not-handled";
  };

  const handleTogggleClick = (e: React.MouseEvent, inlineStyle: string) => {
    e.preventDefault();
    setEditorState(RichUtils.toggleInlineStyle(editorState, inlineStyle));
  };

  const handleBlockClick = (e: React.MouseEvent, blockType: string) => {
    e.preventDefault();
    setEditorState(RichUtils.toggleBlockType(editorState, blockType));
  };

  const onKeyUp = useCallback(
    (e: KeyboardEvent) => {
      e.preventDefault();
      handleSave();
    },
    [handleSave],
  );

  useEffect(() => {
    document.addEventListener("keyup", onKeyUp);
    return () => {
      document.removeEventListener("keyup", onKeyUp);
    };
  }, [onKeyUp]);

  React.useEffect(() => {
    console.log("currentText: ", currentText);
    handleSave();
  }, [currentText]);

  const client = useRef<StompJs.Client | null>(null);
  const [documentIdx, setDocumentIdx] = useState<string>("");

  const connect = (docsIdx: string) => {
    const trimmedDocsIdx = docsIdx;

    if (trimmedDocsIdx && client.current) {
      client.current.activate();
    }
  };
  useEffect(() => {
    client.current = new StompJs.Client({
      brokerURL: "ws://localhost:8080/ws",
    });

    const onConnect = (trimmedDocsIdx: string) => {
      console.log("Connected to WebSocket with", trimmedDocsIdx);
      const docsMessage = {
        documentIdx: trimmedDocsIdx,
      };

      client.current!.publish({
        destination: "/app/chat.showDocs",
        body: JSON.stringify(docsMessage),
      });

      client.current!.subscribe("/topic/public", (docs) => {
        displayDocs(JSON.parse(docs.body));
      });
    };

    client.current.onConnect = () => {
      const docsIdxInput = document.getElementById(
        "docs-idx",
      ) as HTMLInputElement;
      const trimmedDocsIdx = docsIdx;
      onConnect(trimmedDocsIdx);
    };

    client.current.onStompError = onError;

    return () => {
      if (client.current) {
        client.current.deactivate();
      }
    };
  }, []);

  const onError = (error: any) => {
    console.error("Could not connect to WebSocket server:", error);
  };

  let contentText = "";
  const displayDocs = (docs: Docs) => {
    setTitle(docs.title);
    console.log(docs.content);
    contentText = docs.content;
    console.log("contentText : ", contentText);
    try {
      // Try parsing the content as JSON
      const contentObject = JSON.parse(docs.content);
      console.log("obj", contentObject);

      // Check if the parsed content is an object (optional)
      if (typeof contentObject === "object" && contentObject !== null) {
        const contentState = convertFromRaw(contentObject);
        const newEditorState = EditorState.createWithContent(contentState);
        setEditorState(newEditorState);
      } else {
        console.error("Invalid JSON content:", docs.content);
      }
    } catch (error) {
      console.error("Error parsing JSON content:", error);
    }

    console.log("displaydocs");
  };
  useEffect(() => {
    connect(docsIdx);
  }, []);

  const handleContentChange = (
    newContentState: ContentState,
    changeType: EditorChangeType,
  ) => {
    const newEditorState = EditorState.push(
      editorState,
      newContentState,
      changeType,
    );
    setEditorState(newEditorState);
  };

  const handleButtonClick = () => {
    // 현재 에디터의 컨텐츠 가져오기
    const currentContentState = editorState.getCurrentContent();

    // "ㅁㄴㅇㄻㄴㅇㄻㄴㅇㄹ"로 새로운 컨텐츠 생성
    const newContentState = ContentState.createFromText(contentText);

    // 새로운 컨텐츠로 에디터 업데이트
    handleContentChange(newContentState, "insert-characters");
  };

  return (
    <StyledTexteditor className="texteditor">
      <TextTitle titleProps={title} />
      <ButtonContainer>
        <StyledButton onMouseDown={(e) => handleBlockClick(e, "header-one")}>
          H1
        </StyledButton>
        <StyledButton onMouseDown={(e) => handleBlockClick(e, "header-two")}>
          H2
        </StyledButton>
        <StyledButton onMouseDown={(e) => handleBlockClick(e, "header-three")}>
          H3
        </StyledButton>
        <StyledButton onMouseDown={(e) => handleBlockClick(e, "unstyled")}>
          Normal
        </StyledButton>
        <StyledButton onMouseDown={(e) => handleTogggleClick(e, "BOLD")}>
          bold
        </StyledButton>
        <StyledButton onMouseDown={(e) => handleTogggleClick(e, "UNDERLINE")}>
          underline
        </StyledButton>
        <StyledButton onMouseDown={(e) => handleTogggleClick(e, "ITALIC")}>
          italic
        </StyledButton>
        <StyledButton
          onMouseDown={(e) => handleTogggleClick(e, "STRIKETHROUGH")}
        >
          strikethrough
        </StyledButton>
        <StyledButton
          onMouseDown={(e) => handleBlockClick(e, "ordered-list-item")}
        >
          Ordered List
        </StyledButton>
        <StyledButton
          onMouseDown={(e) => handleBlockClick(e, "unordered-list-item")}
        >
          Unordered List
        </StyledButton>
        <StyledButton
          disabled={editorState.getUndoStack().size <= 0}
          onMouseDown={() => setEditorState(EditorState.undo(editorState))}
        >
          undo
        </StyledButton>
        <StyledButton
          disabled={editorState.getRedoStack().size <= 0}
          onMouseDown={() => setEditorState(EditorState.redo(editorState))}
        >
          redo
        </StyledButton>
      </ButtonContainer>
      <Editor
        editorState={editorState}
        onChange={(newEditorState) => {
          setEditorState(newEditorState);
          handleChange(newEditorState);
        }}
        handleKeyCommand={handleKeyCommand}
      />
      <SaveButton
        className="save"
        type="button"
        onClick={(e) => {
          console.log("btn click");
          e.preventDefault();
          handleSave();
          connect(docsIdx);
          handleButtonClick();
        }}
      >
        save
      </SaveButton>
    </StyledTexteditor>
  );
};

export default TextEditor;
