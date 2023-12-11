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

const SaveButton = styled.button`
  background-color: rgb(163, 204, 163);
  color: #333333;
  border-radius: 0.5rem;
`;

const ButtonContainer = styled.div`
  width: 41rem;
`;

interface TextEditorProps {
  id: string;
}

const TextEditor: React.FC<TextEditorProps> = ({ id }) => {
  const [title, setTitle] = useState<string>("");
  const [quill, setQuill] = useState<Quill | null>(null);

  const client = useRef<StompJs.Client | null>(null);
  const docsIdx = id;
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
    setTitle(docs.title);
    console.log(docs.content);
    if (quill) {
      const delta = quill.clipboard.convert(docs.content);
      quill.setContents(delta, "silent");
    }

    console.log("displaydocs");
  };

  useEffect(() => {
    const initializeQuill = () => {
      if (quill) {
        return;
      }

      const editor = new Quill("#quill-editor", {
        theme: "snow",
      });

      editor.on("text-change", () => {
        handleSave(editor.root.innerHTML);
      });

      setQuill(editor);
    };

    initializeQuill();
    connect(docsIdx);
  }, [quill, docsIdx, title]);

  const handleSave = (content: string) => {
    console.log("Saving content:", content);
  };

  const toolbar = document.getElementsByClassName("ql-toolbar");
  if (toolbar.length > 1) {
    toolbar[0].parentNode.removeChild(toolbar[0]);
  }
  return (
    <StyledTexteditor className="texteditor">
      <TextTitle
        titleProps={title}
        onTitleChange={(newTitle) => setTitle(newTitle)}
      />
      <div id="quill-editor" />
      <SaveButton
        className="save"
        type="button"
        onClick={(e) => {
          if (quill) {
            handleSave(quill.root.innerHTML);
          }
        }}
      >
        Save
      </SaveButton>
    </StyledTexteditor>
  );
};

export default TextEditor;
