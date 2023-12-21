import React, { useEffect, useRef, useState } from "react";
import styled from "styled-components";
import * as StompJs from "@stomp/stompjs";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { accessTokenState } from "../../state/authState";

const StyledTexteditor = styled.div`
  width: 41rem;
`;

const TextArea = styled.textarea`
  width: 100%;
  height: 300px;
  border: 1px solid gray;
  padding: 4px;
  font-size: 16px;
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
  const textAreaRef = useRef<HTMLTextAreaElement>(null); // 추가: textArea 참조 생성

  const headers = {
    Authorization: `Bearer ${accessTokenState}`,
  };

  useEffect(() => {
    client.current = new StompJs.Client({
      brokerURL: "ws://localhost:8080/ws",
      // connectHeaders: {
      //   Authorization: `Bearer ${accessToken}`,
      // },
    });

    const onConnect = (trimmedDocsId: string) => {
      console.log("Connected to WebSocket with", trimmedDocsId);
      const docsMessage = {
        documentId: trimmedDocsId,
      };

      client.current!.publish({
        destination: "/app/doc.showDocs",
        body: JSON.stringify(docsMessage),
      });
      console.log("'/app/doc.showDocs'에 publish");

      const displayDocs = (docs: any) => {
        setTitle(docs.title); // docs.title이 undefined나 null이면 빈 문자열을 사용
        setContent(docs.content); // docs.content가 undefined나 null이면 빈 문자열을 사용
      };

      client.current!.subscribe("/topic/public", (docs) => {
        displayDocs(JSON.parse(docs.body));
        console.log("docs.body : ", docs.body);
      });
    };

    const textChange = (trimmedDocsId: string) => {
      const displayDocs = (docs: any) => {
        setTitle(docs.title); // docs.title이 undefined나 null이면 빈 문자열을 사용
        setContent(docs.content); // docs.content가 undefined나 null이면 빈 문자열을 사용
      };
      client.current!.subscribe("/topic/broadcastByTextChange", (docs) => {
        console.log(docs);
        displayDocs(JSON.parse(docs.body));
        console.log("comming docs.body : ", docs.body);
        const docsbody = JSON.parse(docs.body);
        setContent(docsbody.text);
        console.log("content: ", docsbody.title);
      });
    };

    client.current!.onConnect = () => {
      onConnect(docsId);
      textChange(docsId);
    };

    client.current!.activate();
    console.log("title content : ", title, content);

    return () => {
      client.current?.deactivate();
    };
  }, []);

  const handleTextChange = (event: React.ChangeEvent<HTMLTextAreaElement>) => {
    const newText = event.target.value;
    const cursorPosition = event.target.selectionStart; // 현재 커서 위치 저장

    setContent(newText); // 상태 업데이트

    // 마지막 문자가 한글이 아닌지 확인
    const lastChar = newText.charCodeAt(newText.length - 1);
    const isNotKorean =
      lastChar < 0x3131 ||
      (lastChar > 0x3163 && lastChar < 0xac00) ||
      lastChar > 0xd7a3;
    if (client.current && isNotKorean) {
      const message = {
        text: newText,
        documentId: documentsId,
        userId: JSON.parse(localStorage.getItem("user") ?? "").id,
      };

      client.current.publish({
        destination: "/topic/broadcastByTextChange",
        body: JSON.stringify(message),
      });
    }
    if (textAreaRef.current) {
      textAreaRef.current.selectionStart = cursorPosition;
      textAreaRef.current.selectionEnd = cursorPosition;
    }
  };

  const handleTitleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setTitle(event.target.value);
  };

  const handleDelete = async () => {
    const isConfirmed = window.confirm("문서를 삭제하시겠습니까?");
    if (isConfirmed) {
      try {
        await axios.delete(`/team/${teamId}/documents/${documentsId}`, {
          headers,
        });
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

  return (
    <StyledTexteditor>
      <TitleInput
        value={title}
        onChange={handleTitleChange}
        placeholder="제목을 입력하세요"
      />
      <TextArea ref={textAreaRef} value={content} onChange={handleTextChange} />
      <ButtonContainer>
        <StyledButton onClick={handleCommentClick}>댓글</StyledButton>
        <StyledButton onClick={handleDelete}>삭제하기</StyledButton>
      </ButtonContainer>
    </StyledTexteditor>
  );
};

export default TextEditor;
