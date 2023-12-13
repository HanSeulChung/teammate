import React, { useState, useEffect } from "react";
import styled from "styled-components";

// 스타일 컴포넌트 정의
const DocumentContainer = styled.div`
  box-sizing: border-box;
  width: 1024px;
  height: auto;
  display: flex;
  align-content: space-between;
  flex-wrap: wrap;
  margin-bottom: 20px;
`;

const DocumentItem = styled.div`
  width: 100%;
  border: 1px solid black;
  display: flex;
  justify-content: space-between;
  margin: 4px 0;
  padding: 10px;
  border-radius: 12px;
`;

const TitleContentContainer = styled.div`
  flex-grow: 1;
`;
const StyledButton = styled.button`
  background-color: rgb(163, 204, 163);
  color: #333333;
  border-radius: 0.5rem;
  margin: 4px;
`;

const TitleDaytime = styled.p`
  margin: 4px;
  align-self: end;
`;

const DatesContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  min-width: 150px;
`;

// 타입 정의
type Document = {
  documentId: string;
  title: string;
  content: string;
  teamId: string;
  commentsId: string[];
  createdDt: string;
  updatedDt: string;
};

type DocumentListProps = {
  teamId: string;
};

const testdocument = [
  {
    documentId: "생성된 document id",
    title: "제목",
    content: "내용aaaaaaaaaaaaaaaaaaaaaaa",
    teamId: "team id",
    commentsId: ["commentid 1", "commentid 2"],
    createdDt: "생성 날짜",
    updatedDt: "수정 날짜",
  },
  {
    documentId: "생성된 document id",
    title: "제목2",
    content: "내용2aaaaaaaaaaaaaaaaaaa",
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

// 컴포넌트
const DocumentList: React.FC<DocumentListProps> = ({ teamId }) => {
  const [documents, setDocuments] = useState<Document[]>(testdocument);
  const [currentPage, setCurrentPage] = useState(0);
  const pageSize = 10;

  useEffect(() => {
    const url = `/team/${teamId}/documents?page=${currentPage}&size=10`;
    fetch(url)
      .then((response) => response.json())
      .then((data) => setDocuments(data))
      .catch((error) => console.error("Error fetching data: ", error));
  }, []);

  const totalPages = Math.ceil(documents.length / pageSize);
  const currentDocuments = documents.slice(
    currentPage * pageSize,
    (currentPage + 1) * pageSize,
  );

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  const renderPagination = () => {
    let pages = [];
    for (let i = 0; i < totalPages; i++) {
      pages.push(
        <button key={i} onClick={() => handlePageChange(i)}>
          {i + 1}
        </button>,
      );
    }
    return <div>{pages}</div>;
  };

  return (
    <>
      <StyledButton>Add docs</StyledButton>
      <DocumentContainer>
        {currentDocuments.map((doc) => (
          <DocumentItem key={doc.documentId}>
            <TitleContentContainer>
              <h2>{doc.title}</h2>
              <p>{doc.content}</p>
            </TitleContentContainer>
            <DatesContainer>
              <TitleDaytime>Created: {doc.createdDt}</TitleDaytime>
              <TitleDaytime>Updated: {doc.updatedDt}</TitleDaytime>
            </DatesContainer>
          </DocumentItem>
        ))}
      </DocumentContainer>
      {renderPagination()}
    </>
  );
};

export default DocumentList;
