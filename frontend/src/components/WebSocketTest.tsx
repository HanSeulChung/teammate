// import { CompatClient, Stomp } from "@stomp/stompjs";

import React, { useRef, useState, FormEvent, useEffect } from 'react';
import SockJS from 'sockjs-client';
import * as StompJs from "@stomp/stompjs";



interface Docs {
  title: string;
  content: string;
}

const MyComponent: React.FC = () => {
  const client = useRef<StompJs.Client | null>(null);
  const [documentIdx, setDocumentIdx] = useState<string>("");

  const connect = (event: FormEvent) => {
    event.preventDefault();

    const docsIdxInput = document.getElementById('docs-idx') as HTMLInputElement;
    const trimmedDocsIdx = docsIdxInput.value.trim();
    
    if (trimmedDocsIdx && client.current) {
      client.current.activate();
    }
  };

  useEffect(() => {
    client.current = new StompJs.Client({
      brokerURL: 'ws://localhost:8080/ws'
    });
  
    const onConnect = (trimmedDocsIdx: string) => {
      console.log('Connected to WebSocket with', trimmedDocsIdx);
      const docsMessage = {
        documentIdx: trimmedDocsIdx,
      };

      client.current!.publish({
        destination: "/app/chat.showDocs",
        body: JSON.stringify(docsMessage),
      });
  
      client.current!.subscribe('/topic/public', (docs) => {
        displayDocs(JSON.parse(docs.body));
      });
    };
  
    client.current.onConnect = () => {
      const docsIdxInput = document.getElementById('docs-idx') as HTMLInputElement;
      const trimmedDocsIdx = docsIdxInput.value.trim();
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
    console.error('Could not connect to WebSocket server:', error);
    // 에러 처리 로직
  };

  const displayDocs = (docs: Docs) => {
    const docsTitleElement = document.getElementById('docsTitle');
    const docsContentElement = document.getElementById('docsContent');

    if (docsTitleElement && docsContentElement) {
      docsTitleElement.textContent = docs.title;
      docsContentElement.textContent = docs.content;
    }
  };

  return (
    <form id="docsIdxForm" onSubmit={connect}>
      <div id="docs-idx-page">
        <div className="docs-idx-page-container">
          <h1 className="title">Type your docs documentIdx</h1>
          <div className="form-group">
            <input type="text" id="docs-idx" placeholder="Enter Document ID" autoComplete="off" className="form-control" />
          </div>
          <div className="form-group">
            <button type="submit" className="accent docs-idx-submit">Start Bring Docs</button>
          </div>
        </div>
      </div>

      <div id="docs-page" className="hidden">
        <div className="docs-container">
          <div className="docs-header">
            <h2 id="docsTitle">Document Title</h2>
          </div>
          <div className="docs-content" id="docsContent">
            Document Content
          </div>
        </div>
      </div>
    </form>
  );
};

export default MyComponent;