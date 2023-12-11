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

  const docsIdx = id;
  console.log(docsIdx);

  const client = useRef<StompJs.Client | null>(null);

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
      onConnect(docsIdx);
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

  const displayDocs = (docs: Docs) => {
    setTitle(docs.title);
    console.log(docs.content);

    if (quill) {
      quill.clipboard.dangerouslyPasteHTML(docs.content);
    }

    console.log("displaydocs");
  };

  useEffect(() => {
    connect(docsIdx);
  }, []);

  useEffect(() => {
    if (!quill) {
      const editor = new Quill("#quill-editor", {
        // theme: "snow", // or use another theme if you prefer
      });

      editor.on("text-change", () => {
        handleSave(editor.root.innerHTML);
      });

      setQuill(editor);
    }
  }, [quill]);

  const handleSave = (content: string) => {
    // Handle save logic here with the content
    console.log("Saving content:", content);
  };

  return (
    <StyledTexteditor className="texteditor">
      <TextTitle titleProps={title} />
      <ButtonContainer></ButtonContainer>
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
