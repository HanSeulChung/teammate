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
import { useRecoilValue } from "recoil";

interface TextEditorProps {
  teamId: string;
  documentsId: string;
}

const TextEditor: React.FC<TextEditorProps> = ({ teamId, documentsId }) => {
  const [title, setTitle] = useState<string>("");
  const [content, setContent] = useState<string>("");
  const docsId = documentsId;
  const client = useRef<StompJs.Client | null>(null);
  const quillRef = useRef<ReactQuill>(null);
  const navigate = useNavigate();
  const accessToken = window.sessionStorage.getItem("accessToken");
  const [participantIds, setParticipantIds] = useState<number>();

  useEffect(() => {
    client.current = new StompJs.Client({
      brokerURL: "ws://localhost:8080/ws",
      connectHeaders: {
        Authorization: `Bearer ${accessToken}`,
      },
    });

    client.current.onStompError = (frame) => {
      console.error("WebSocket Error:", frame);

      // 에러 메시지가 특정 문자열을 포함하는지 확인하여 처리
      if (frame.headers?.message?.includes("Invalid access token")) {
        // accessToken이 만료되었을 때, accessToken이 올바르지 않은 형태일때
        console.log("Invalid access token detected.");
        // 토큰 재발급 요청으로 accessToken을 저장을 다시 해놓고 문서 목록으로 redirect .. 

      } else if (frame.headers?.message?.includes("No or invalid Authorization header")){
        // Bearer "accessToken"과 같은 규격이 아닐때
        console.log("No or invalid Authorization header detected");

      } else if (frame.headers?.message?.includes("Authentication failed")){
        // 토큰 값으로 인증이 실패 했을 경우
        console.log("Authentication failed detected");
        // 문서 목록으로 redirect
      } else {
        // 다른 에러 처리
      }
    };

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
        // const quill = quillRef.current?.getEditor();
        // if (quill) {
        //   const initContent = quill.clipboard.convert(docs.content);
        //   const initTitle = docs.title;
        //   quill.setContents(initContent);
        //   setTitle(initTitle);
        // }
        setTitle(docs.title);
        setContent(docs.content);
      };

      client.current!.subscribe("/topic/display", (docs) => {
        displayDocs(JSON.parse(docs.body));
        //console.log("docs.body : ", docs.body);
      });
    };

    const textChange = (trimmedDocsId: string) => {
      const currentUserId = JSON.parse(sessionStorage.getItem("user") ?? "").id;

      client.current!.subscribe("/topic/broadcastByTextChange", (docs) => {
        const docsbody = JSON.parse(docs.body);
        
        if (docsbody.memberId !== currentUserId) { 
          // 다른 사용자가 보낸 메시지일 때만 상태 업데이트
          console.log("broadCast를 이렇게 받았다!", docsbody);
          // console.log(docsbody.content.replace(/(^([ ]*<p><br><\/p>)*)|((<p><br><\/p>)*[ ]*$)/gi, "").trim(" "));
          setTitle(docsbody.title);
          // setContent(docsbody.content.replace(/(^([ ]*<p><br><\/p>)*)|((<p><br><\/p>)*[ ]*$)/gi, "").trim(" ")); // 엔터만 (연속적으로) 했을 때 생기는 에러 해결
          setContent(docsbody.content); 
        }
      });
    };

    const fetchParticipants = async () => {
      try {
        const response = await axiosInstance.get("/member/participants", {});
        // URL에서 가져온 teamId를 number 타입으로 변환
        const currentTeamId = Number(teamId);
        const participant = response.data.find(
          (item: { teamId: number }) => item.teamId === currentTeamId,
        );

        setParticipantIds(participant ? participant.teamParticipantsId : null);

      } catch (error) {
        console.error("Error fetching participants:", error);
      }
    };

    client.current!.onConnect = () => {
      onConnect(docsId);
      textChange(docsId);
      fetchParticipants();
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
        memberEmail: JSON.parse(sessionStorage.getItem("user") ?? "").id,
        title: title,
        content: newText,
        documentId: documentsId,
      };
      console.log("message : ", message);

      client.current.publish({
        destination: "/app/doc.updateDocsByTextChange",
        body: JSON.stringify(message),
      });
    }
  };

  const handleTitleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const newTitle = event.target.value;
    setTitle(newTitle);
    setContent(content);

    if (client.current) {
      const message = {
        memberEmail: JSON.parse(localStorage.getItem("user") ?? "").id,
        title: newTitle,
        content: content,
        documentId: documentsId,
      };

      client.current.publish({
        destination: "/app/doc.updateDocsByTextChange",
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
    if (client.current) {
      const contentCopy = content.slice(); // content의 복사본을 만듦
      const message = {
        memberEmail: JSON.parse(sessionStorage.getItem("user") ?? "").id,
        title: title,
        content: contentCopy.replace(/<p>/g, "").replace(/<\/p>/g, "").replace(/<br>/g, "\n"),
        documentId: documentsId,
        participantsId : participantIds
      };

      client.current.publish({
        destination: "/app/doc.saveDocs",
        body: JSON.stringify(message),
      });

    }


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
