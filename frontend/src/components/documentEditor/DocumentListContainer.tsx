import React from "react";
import styled from "styled-components";
import DocumentList from "./DocumentList";

interface TextEditorViewProps {}

const testdocument = [
  {
    documentId: "생성된 document id",
    title: "제목",
    content: "내용",
    teamId: "team id",
    commentsId: ["commentid 1", "commentid 2"],
    createdDt: "생성 날짜",
    updatedDt: "수정 날짜",
  },
  {
    documentId: "생성된 document id",
    title: "제목2",
    content: "내용2",
    teamId: "team id2",
    commentsId: ["commentid 1", "commentid 2"],
    createdDt: "생성 날짜2",
    updatedDt: "수정 날짜2",
  },
  {
    documentId: "생성된 document id",
    title: "제목2",
    content: "내용2",
    teamId: "team id2",
    commentsId: ["commentid 1", "commentid 2"],
    createdDt: "생성 날짜2",
    updatedDt: "수정 날짜2",
  },
  {
    documentId: "생성된 document id",
    title: "제목2",
    content: "내용2",
    teamId: "team id2",
    commentsId: ["commentid 1", "commentid 2"],
    createdDt: "생성 날짜2",
    updatedDt: "수정 날짜2",
  },
  {
    documentId: "생성된 document id",
    title: "제목2",
    content: "내용2",
    teamId: "team id2",
    commentsId: ["commentid 1", "commentid 2"],
    createdDt: "생성 날짜2",
    updatedDt: "수정 날짜2",
  },
  {
    documentId: "생성된 document id",
    title: "제목2",
    content: "내용2",
    teamId: "team id2",
    commentsId: ["commentid 1", "commentid 2"],
    createdDt: "생성 날짜2",
    updatedDt: "수정 날짜2",
  },
  {
    documentId: "생성된 document id",
    title: "제목2",
    content: "내용2",
    teamId: "team id2",
    commentsId: ["commentid 1", "commentid 2"],
    createdDt: "생성 날짜2",
    updatedDt: "수정 날짜2",
  },
];

const DocumentListContainer: React.FC<TextEditorViewProps> = () => {
  return <DocumentList documents={testdocument} />;
};

export default DocumentListContainer;
