import React, { useEffect, useRef, useState } from "react";
import styled from "styled-components";
import "quill/dist/quill.snow.css";
import "./TextEditor.css";
import TextTitle from "./TextTitle";
import * as StompJs from "@stomp/stompjs";
import Quill from "quill";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const StyledTexteditor = styled.div`
  width: 41rem;
`;

const TitleInput = styled.input`
  border: 1px solid black;
  background-color: white;
  color: black;
  width: 646px;
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

const QuillStyled = styled.div`
  height: auto;
`;

interface TextEditorProps {
  teamId: string;
  documentsId: string;
}

const TextEditor: React.FC<TextEditorProps> = ({ teamId, documentsId }) => {
  const [title, setTitle] = useState<string>("");
  const [content, setContent] = useState<string>("");

  const client = useRef<StompJs.Client | null>(null);
  const teamid = teamId;
  const document = documentsId;
  const url = `/team/${teamid}/documents/${document}`;

  const docsId = documentsId;
  const connect = (docsId: string) => {
    const trimmedDocsId = docsId;

    // if (trimmedDocsId && client.current) {
    //   client.current.activate();
    //   console.log(" client.current.activate()");
    // }
  };

  useEffect(() => {

    // client.current = new StompJs.Client({
    //   brokerURL: "ws://118.67.128.124:8080/ws",
    //   // connectHeaders: {
    //   //   Authorization: `Bearer ${accessToken}`,
    //   // },
    // });

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

      client.current!.subscribe("/topic/public", (docs) => {
        displayDocs(JSON.parse(docs.body));
        console.log("docs.body : ", docs.body);
      });
    };


    client.current.onConnect = () => {
      onConnect(docsId);
    };

    client.current.onStompError = onError;

    // 웹 소켓 연결 및 구독 처리를 마운트 시에 실행되도록 변경
    client.current.activate();

    return () => {
      if (client.current) {
        client.current.deactivate();
      }
    };
  }, [docsId]);

  const onError = (error: any) => {
    console.error("Could not connect to WebSocket server:", error);
  };

  const displayDocs = (docs: Docs) => {
    setTitle(docs.title);
    setContent(docs.content);
  };

  useEffect(() => {
    const initializeQuill = () => {
      // if (!title || !content) {
      //   return;
      // }

      const editor = new Quill("#quill-editor", {
        theme: "snow",
      });

      editor.setText(content);

      editor.on("text-change", (delta, oldDelta, source ) => {
        if (source != 'user') {

        }
        console.log("text-change가 감지되었습니다.");
        console.log("변화한 내용:", delta);
        handleTextChange(editor, delta); // 변경된 delta 값을 전달합니다.
      });


      editor.on('selection-change', (delta, oldDelta, source) => {
        if (source === 'user') {
          // 선택 변경이 발생했을 때의 처리
          console.log('selection-change:', delta);
          handleSelectionChange(editor, delta);
        }
      });

      // editor.setText(content);
    };

    connect(docsId);
    initializeQuill();
  }, [content]);

  const handleTextChange = (editor: Quill, delta: any) => {
    if (client.current) {
      console.log("range", delta);
      const content = editor.getContents(); // 새로운 내용을 가져옵니다.

      client.current!.publish({
        destination: '/app/doc.updateDocsByTextChange',
        body: JSON.stringify({ eventName: 'text-change', deltaValue: delta }),
      });
      console.log('delta sent to server:', delta);
      var Delta = Quill.import('delta');  
      client.current!.subscribe("/topic/broadcastByTextChange", function(data) {
        console.log("deltaMsg 브로드 캐스팅 받았음 - handleTextChange");
        const textChangeData = JSON.parse(data.body).deltaValue;

        const keys = Object.keys( textChangeData.ops[1]);
        if (keys.includes('insert')) {
          const insertDelta = new Delta()
          .retain(textChangeData.ops[0].retain)
          .insert(textChangeData.ops[1].insert);
          console.log(insertDelta);
          editor.updateContents(insertDelta);

        } else {
          const deleteDelta = new Delta()
            .retain(textChangeData.ops[0].retain)
            .delete(textChangeData.ops[1].delete);
            console.log(deleteDelta);
          editor.updateContents(deleteDelta);
        }
      });

      // handleSave(delta);
    }
  };

  const handleSelectionChange = (editor: Quill, delta: any) => {
    if (client.current) {
      console.log("delta", delta);

      client.current!.publish({
        destination: '/app/doc.updateDocsBySelectionChange',
        body: JSON.stringify({ 
          eventName: 'selection-change',  
          deltaValue: {
            index: delta.index, 
            length: delta.length
          }
      }),
      });
      console.log('delta sent to server:', delta);

      client.current!.subscribe("/topic/broadcastBySelectionChange", (data) => {
        console.log("deltaMsg 브로드 캐스팅 받았음 - handleSelectionChange");
        console.log("data is ", data.body);
        
        // 서버로부터 받은 데이터 객체를 JSON 파싱
        const selectionChangeData = JSON.parse(data.body);

        const index = selectionChangeData.index;
        const length = selectionChangeData.length;
        console.log("Index:", index);
        console.log("Length:", length);
        editor.setSelection(index, length);
      });
    }
  };

  const handleSave = (delta: string) => {
    console.log("delta: ", delta);

    if (client.current) {
      console.log("Saving content:", content);
      client.current!.publish({
        destination: "/app/doc.saveDocs",
        body: JSON.stringify({
          id: docsId,
          title: title,
          content: content,
          editorEmail: "w0w1278@naver.com",
          teamId: teamid,
        }),
      });
      console.log(
        "changedDocument : ",
        JSON.stringify({
          id: docsId,
          title: title,
          content: content,
          editorEmail: "w0w1278@naver.com",
          teamId: teamid,
        })
      );
    }
  };

  const handleDelete = async () => {
    const isConfirmed = window.confirm("문서를 삭제하시겠습니까?");
    if (isConfirmed) {
      try {
        const response = await axios.delete(
          `/team/${teamId}/documents/${documentsId}`,
          {
            headers: {
              // Authorization: `Bearer ${accessToken}`, // accessToken 변수는 유효한 토큰으로 설정되어야 합니다.
            },
          },
        );
        console.log(response.data.message); // 성공 메시지 로깅
        navigate(`/team/${teamId}/documentsList`);
      } catch (error) {
        console.error("문서 삭제에 실패했습니다:", error);
        // 오류 처리 로직, 예: 오류 메시지 표시
      }
    }
  };

  const navigate = useNavigate();

  const handleCommentClick = () => {
    const currentPath = window.location.pathname;
    navigate(`${currentPath}/comment`);
  };

  const handleTitleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setTitle(event.target.value);
  };

  return (
    <StyledTexteditor
      className="texteditor"
      onChange={handleTitleChange}
      placeholder="제목을 입력하세요"
    >
      <TitleInput value={title}  onChange={handleTitleChange} />
      <QuillStyled id="quill-editor" />
      <ButtonContainer>
        <div>
          <StyledButton onClick={handleCommentClick}>댓글</StyledButton>
        </div>
        <StyledButton onClick={handleDelete}>삭제하기</StyledButton>
      </ButtonContainer>
    </StyledTexteditor>
  );
};

export default TextEditor;
