// import React, { useCallback, useEffect, useState } from "react";
// import Quill from "quill";
// import "quill/dist/quill.snow.css";
// import * as StompJs from "@stomp/stompjs";
// import SockJS from "sockjs-client";
// import { useParams } from "react-router-dom";

// interface TextEditorProps {}

// const SAVE_INTERVAL_MS = 2000;
// const TOOLBAR_OPTIONS = [
//   [{ header: [1, 2, 3, 4, 5, 6, false] }],
//   [{ font: [] }],
//   [{ list: "ordered" }, { list: "bullet" }],
//   ["bold", "italic", "underline"],
//   [{ color: [] }, { background: [] }],
//   [{ script: "sub" }, { script: "super" }],
//   [{ align: [] }],
//   ["image", "blockquote", "code-block"],
//   ["clean"],
// ];

// const TextEditor: React.FC<TextEditorProps> = () => {
//   const { id: documentId } = useParams<{ id: string }>();
//   const [stompClient, setStompClient] = useState<StompJs.Client | null>(null);
//   const [quill, setQuill] = useState<Quill | null>(null);

//   useEffect(() => {
//     const socket = new SockJS("http://localhost:8080/ws");
//     const stomp = StompJs.over(socket);
//     setStompClient(stomp);

//     return () => {
//       stomp.disconnect();
//     };
//   }, []);

//   useEffect(() => {
//     if (stompClient == null || quill == null) return;

//     stompClient.connect({}, (frame) => {
//       console.log("Connected to WebSocket");
//       stompClient.subscribe(`/topic/document/${documentId}`, (message) => {
//         const document = JSON.parse(message.body);
//         quill.setContents(document);
//         quill.enable();
//       });

//       stompClient.send(`/app/document/${documentId}`, {}, "");
//     });

//     return () => {
//       if (stompClient) {
//         stompClient.disconnect();
//       }
//     };
//   }, [stompClient, quill, documentId]);

//   useEffect(() => {
//     if (stompClient == null || quill == null) return;

//     const interval = setInterval(() => {
//       stompClient.send(
//         `/app/save-document/${documentId}`,
//         {},
//         JSON.stringify(quill.getContents()),
//       );
//     }, SAVE_INTERVAL_MS);

//     return () => {
//       clearInterval(interval);
//     };
//   }, [stompClient, quill, documentId]);

//   useEffect(() => {
//     if (stompClient == null || quill == null) return;

//     const handler = (message: StompJs.Message) => {
//       const delta = JSON.parse(message.body);
//       quill.updateContents(delta);
//     };

//     stompClient.subscribe(`/topic/changes/${documentId}`, handler);

//     return () => {
//       stompClient.unsubscribe(`/topic/changes/${documentId}`, handler);
//     };
//   }, [stompClient, quill, documentId]);

//   useEffect(() => {
//     if (stompClient == null || quill == null) return;

//     const handler = (delta: any, oldDelta: any, source: string) => {
//       if (source !== "user") return;
//       stompClient.send(
//         `/app/send-changes/${documentId}`,
//         {},
//         JSON.stringify(delta),
//       );
//     };

//     quill.on("text-change", handler);

//     return () => {
//       quill.off("text-change", handler);
//     };
//   }, [stompClient, quill, documentId]);

//   const wrapperRef = useCallback((wrapper: HTMLDivElement | null) => {
//     if (wrapper == null) return;

//     wrapper.innerHTML = "";
//     const editor = document.createElement("div");
//     wrapper.append(editor);
//     const q = new Quill(editor, {
//       theme: "snow",
//       modules: { toolbar: TOOLBAR_OPTIONS },
//     });
//     q.disable();
//     q.setText("Loading...");
//     setQuill(q);
//   }, []);

//   return <div className="container" ref={wrapperRef}></div>;
// };

// // export default TextEditor;
