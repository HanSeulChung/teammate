import React, { useEffect, useRef, useState } from "react";
import styled from "styled-components";
import "quill/dist/quill.snow.css";
import "./TextEditor.css";
import TextTitle from "./TextTitle";
import * as StompJs from "@stomp/stompjs";
import Quill from "quill";

const StyledTexteditor = styled.div`
  width: 41rem;
`;

const StyledButton = styled.button`
  background-color: rgb(163, 204, 163);
  color: #333333;
  border-radius: 0.5rem;
  margin: 4px;
`;

const ButtonContainer = styled.div`
  display: flex;
  justify-content: space-between;
  width: 41rem;
  margin-top: 10px;
`;

interface TextEditorProps {
  teamId: string;
  documentsId: string;
}

const TextEditor: React.FC<TextEditorProps> = ({ teamId, documentsId }) => {
  const [title, setTitle] = useState<string>("");
  const [quilll, setQuill] = useState<Quill | null>(null);
  const [content, setContent] = useState<string>("");

  const client = useRef<StompJs.Client | null>(null);
  const id = teamId;
  const docsIdx = documentsId;
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
        console.log("docs.body : ", docs.body);
      });
    };

    client.current.onConnect = () => {
      onConnect(docsIdx);
    };

    client.current.onStompError = onError;

    return () => {
      if (client.current) {
        client.current.deactivate();
      }
    };
  }, [docsIdx]);

  const onError = (error: any) => {
    console.error("Could not connect to WebSocket server:", error);
  };

  const displayDocs = (docs: Docs) => {
    setTitle(docs.title); // onchange로 inputtext를 위한 change 를 쓰고 싶으면
    setContent(docs.content); // title, content 대신 객체를 받아와서 state로 관리
  };

  useEffect(() => {
    const initializeQuill = () => {
      if (!title || !content) {
        // 여기도 객체로 관리 / 객체에서 꺼내서 디스트럭쳐링
        return;
      }

      const editor = new Quill("#quill-editor", {
        theme: "snow",
      });

      editor.on("text-change", () => {
        handleSave(editor.root.innerHTML);
      });

      setQuill(editor);

      editor.setText(content);
    };

    connect(docsIdx);
    initializeQuill();
  }, [content]); // 디펜던시도 객체로 관리

  const handleSave = (content: string) => {
    console.log("Saving content:", content);

    if (client.current && quilll) {
      client.current.publish({
        destination: "/app/chat.showDocs",
        body: JSON.stringify({ documentIdx: docsIdx }),
      });
      console.log("json : ", JSON.stringify({ documentIdx: docsIdx }));
    }
  };

  return (
    <StyledTexteditor className="texteditor">
      <TextTitle
        titleProps={title}
        onTitleChange={(newTitle) => setTitle(newTitle)} // onInputChange() => setTitle()
      />
      <div id="quill-editor" />
      <ButtonContainer>
        <div>
          <StyledButton
            className="save"
            type="button"
            onClick={(e) => {
              if (quilll) {
                handleSave(quilll.root.innerHTML);
              }
            }}
          >
            Save
          </StyledButton>
          <StyledButton>comment</StyledButton>
        </div>

        <StyledButton>삭제하기</StyledButton>
      </ButtonContainer>
    </StyledTexteditor>
  );
};

export default TextEditor;
