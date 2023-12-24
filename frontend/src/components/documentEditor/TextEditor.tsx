import React, { useEffect, useRef, useState } from "react";
import styled from "styled-components";
import * as StompJs from "@stomp/stompjs";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { accessTokenState } from "../../state/authState";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import axiosInstance from "../../axios";
import "./ReactQuill.css";

const StyledTexteditor = styled.div`
  width: 41rem;
`;

const TextArea = styled.textarea`
  width: 100%;
  height: 300px;
  border: 1px solid gray;
  padding: 4px;
  font-size: 16px;
  background-color: white;
`;

const TitleInput = styled.input`
  border: 1px solid black;
  background-color: white;
  color: black;
  width: 100%;
  font-size: 16px;
  margin-bottom: 4px;
  border: 1px solid gray;
  padding: 4px;
  ::placeholder {
    color: gray;
  }
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
  const [content, setContent] = useState<string>("");
  const docsId = documentsId;
  const client = useRef<StompJs.Client | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    client.current = new StompJs.Client({
      brokerURL: "ws://118.67.128.124:8080/ws",
      // connectHeaders: {
      //   Authorization: `Bearer ${accessTokenState}`,
      // },
    });

    const onConnect = (trimmedDocsId: string) => {
      //console.log("Connected to WebSocket with", trimmedDocsId);
      const docsMessage = {
        documentId: trimmedDocsId,
      };

      client.current!.publish({
        destination: "/app/doc.showDocs",
        body: JSON.stringify(docsMessage),
      });
      //console.log("'/app/doc.showDocs'에 publish");

      const displayDocs = (docs: any) => {
        setTitle(docs.title);
        setContent(docs.content);
      };

      client.current!.subscribe("/topic/public", (docs) => {
        displayDocs(JSON.parse(docs.body));
        //console.log("docs.body : ", docs.body);
      });
    };

    const textChange = (trimmedDocsId: string) => {
      const currentUserId = JSON.parse(localStorage.getItem("user") ?? "").id;

      client.current!.subscribe("/topic/broadcastByTextChange", (docs) => {
        const docsbody = JSON.parse(docs.body);
        if (docsbody.memberId !== currentUserId) {
          // 다른 사용자가 보낸 메시지일 때만 상태 업데이트
          setTitle(docsbody.title);
          setContent(docsbody.text);
        }
      });
    };

    client.current!.onConnect = () => {
      onConnect(docsId);
      textChange(docsId);
    };

    client.current!.activate();
    //console.log("title content : ", title, content);

    return () => {
      client.current?.deactivate();
    };
  }, []);

  const handleTextChange = (
    content: any,
    delta: any,
    source: any,
    editor: any,
  ) => {
    const newText = content;

    setContent(newText); // 상태 업데이트

    if (client.current) {
      const message = {
        memberId: JSON.parse(localStorage.getItem("user") ?? "").id,
        // title: title,
        text: newText,
        documentId: documentsId,
      };
      //console.log("message : ", message);

      client.current.publish({
        destination: "/topic/broadcastByTextChange",
        body: JSON.stringify(message),
      });
    }
  };

  const handleTitleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const newTitle = event.target.value;
    setTitle(newTitle);

    setContent(content.replace(/<p>/g, "").replace(/<\/p>/g, "\n"));

    if (client.current) {
      const message = {
        memberId: JSON.parse(localStorage.getItem("user") ?? "").id,
        title: newTitle,
        text: content,
        documentId: documentsId,
      };

      client.current.publish({
        destination: "/topic/broadcastByTextChange",
        body: JSON.stringify(message),
      });
    }
  };

  const handleDelete = async () => {
    const isConfirmed = window.confirm("문서를 삭제하시겠습니까?");
    if (isConfirmed) {
      try {
        await axiosInstance.delete(`/team/${teamId}/documents/${documentsId}`);
        navigate(`/team/${teamId}/documentsList`);
      } catch (error) {
        console.error("Error deleting document:", error);
      }
    }
  };

  const handleCommentClick = () => {
    const currentPath = window.location.pathname;
    navigate(`${currentPath}/comment`);
  };

  const handleSaveAndExit = async () => {
    navigate(`/team/${teamId}/documentsList`);
  };

  return (
    <StyledTexteditor>
      <TitleInput
        value={title}
        onChange={handleTitleChange}
        placeholder="제목을 입력하세요"
      />
      <ReactQuill
        value={content}
        onChange={handleTextChange}
        preserveWhitespace={true}
      />
      <ButtonContainer>
        <StyledButton onClick={handleCommentClick}>댓글</StyledButton>
        <StyledButton onClick={handleSaveAndExit}>저장 후 나가기</StyledButton>
        <StyledButton onClick={handleDelete}>삭제하기</StyledButton>
      </ButtonContainer>
    </StyledTexteditor>
  );
};

export default TextEditor;
