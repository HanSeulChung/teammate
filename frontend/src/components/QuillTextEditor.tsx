import React, { useCallback, useEffect, useState } from "react";
import Quill from "quill";
import "quill/dist/quill.snow.css";
import { Client, Message } from "@stomp/stompjs";
import { useParams } from "react-router-dom";
import TextTitle from "./TextTitle";
import styled from "styled-components";

const StyledTexteditor = styled.div``;

interface TextEditorProps {}

const SAVE_INTERVAL_MS = 2000;
const TOOLBAR_OPTIONS = [
  [{ header: [1, 2, 3, 4, 5, 6, false] }],
  [{ font: [] }],
  [{ list: "ordered" }, { list: "bullet" }],
  ["bold", "italic", "underline"],
  [{ color: [] }, { background: [] }],
  [{ script: "sub" }, { script: "super" }],
  [{ align: [] }],
  ["image", "blockquote", "code-block"],
  ["clean"],
];

const TextEditor: React.FC<TextEditorProps> = () => {
  const { id: documentId } = useParams<{ id: string }>();
  const [stompClient, setStompClient] = useState<Client | null>(null);
  const [quill, setQuill] = useState<Quill | null>(null);

  useEffect(() => {
    const stomp = new Client({
      brokerURL: "ws://localhost:8080/ws",
      connectHeaders: {},
      debug: (str) => {
        console.log(str);
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    });

    setStompClient(stomp);

    return () => {
      if (stomp.connected) {
        stomp.deactivate();
      }
    };
  }, []);

  useEffect(() => {
    if (!stompClient || !quill) return;

    stompClient.onConnect = () => {
      stompClient.subscribe(
        `/topic/document/${documentId}`,
        (message: Message) => {
          const document = JSON.parse(message.body);
          quill.setContents(document);
          quill.enable();
        },
      );
      stompClient.subscribe(
        `/user/topic/document/${documentId}`,
        (message: Message) => {
          const delta = JSON.parse(message.body);
          quill.updateContents(delta);
        },
      );

      // Request the current document
      stompClient.publish({
        destination: `/app/document/${documentId}`,
        body: JSON.stringify({}),
      });
    };

    stompClient.activate();

    return () => {
      stompClient.deactivate();
    };
  }, [stompClient, quill, documentId]);

  useEffect(() => {
    if (!stompClient || !quill) return;

    const interval = setInterval(() => {
      stompClient.publish({
        destination: `/app/save/${documentId}`,
        body: JSON.stringify(quill.getContents()),
      });
    }, SAVE_INTERVAL_MS);

    return () => {
      clearInterval(interval);
    };
  }, [stompClient, quill, documentId]);

  const wrapperRef = useCallback((wrapper) => {
    if (wrapper == null) return;

    wrapper.innerHTML = "";
    const editor = document.createElement("div");
    wrapper.append(editor);
    const q = new Quill(editor, {
      theme: "snow",
      modules: { toolbar: TOOLBAR_OPTIONS },
    });
    q.disable();
    q.setText("Loading...");
    setQuill(q);
  }, []);

  // 콘솔에 메시지 출력
  useEffect(() => {
    if (!stompClient || !quill) return;

    stompClient.onConnect = () => {
      console.log("connected to server", documentId);

      stompClient.subscribe(
        `/topic/document/${documentId}`,
        (message: Message) => {
          const document = JSON.parse(message.body);
          quill.setContents(document);
          quill.enable();
        },
      );
      stompClient.subscribe(
        `/user/topic/document/${documentId}`,
        (message: Message) => {
          const delta = JSON.parse(message.body);
          quill.updateContents(delta);
        },
      );

      // Request the current document
      stompClient.publish({
        destination: `/app/document/${documentId}`,
        body: JSON.stringify({}),
      });
    };

    stompClient.onStompError = (frame) => {
      console.log("Stomp error:", frame.headers["message"]);
    };

    stompClient.onWebSocketError = (event) => {
      console.log("WebSocket error:", event);
    };

    stompClient.onUnhandledMessage = (message: Message) => {
      console.log("Unhandled message:", message.body);
    };

    stompClient.activate();

    return () => {
      stompClient.deactivate();
    };
  }, [stompClient, quill, documentId]);

  return (
    <StyledTexteditor>
      <TextTitle titleProps="default title" />
      <div className="container" ref={wrapperRef}></div>
    </StyledTexteditor>
  );
};

export default TextEditor;
