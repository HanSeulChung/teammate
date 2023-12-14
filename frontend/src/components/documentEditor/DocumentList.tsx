import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import styled from "styled-components";
import { useRecoilValue } from "recoil";
import { accessTokenState } from "./atoms";

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

const Container = styled.section`
  min-height: 800px;
`;

const SearchInput = styled.input`
  background-color: white;
  width: 50%;
  height: 28px;
  color: black;
  font-size: 16px;
  border-radius: 8px;
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
  const [filteredDocuments, setFilteredDocuments] = useState<Document[]>([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(0);
  const pageSize = 10;
  const accessToken = useRecoilValue(accessTokenState);

  useEffect(() => {
    const url = `/team/${teamId}/documents?page=${currentPage}&size=10`;
    fetch(url)
      .then((response) => response.json())
      .then((data) => {
        setDocuments(data);
        setFilteredDocuments(data);
      })
      .catch((error) => console.error("Error fetching data: ", error));
  }, [currentPage, teamId]);

  useEffect(() => {
    const filtered = documents.filter(
      (doc) =>
        doc.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
        doc.content.toLowerCase().includes(searchTerm.toLowerCase()),
    );
    setFilteredDocuments(filtered);
  }, [searchTerm, documents]);

  const handlePageChange = (page: React.SetStateAction<number>) => {
    setCurrentPage(page);
  };

  const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(event.target.value);
  };

  const renderPagination = () => {
    const totalPages = Math.ceil(filteredDocuments.length / pageSize);
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

  const currentDocuments = filteredDocuments.slice(
    currentPage * pageSize,
    (currentPage + 1) * pageSize,
  );

  const navigate = useNavigate();

  useEffect(() => {
    const fetchDocuments = async () => {
      try {
        const response = await fetch(
          `/team/${teamId}/documents?page=${currentPage}&size=10`,
          {
            headers: {
              Authorization: `Bearer ${accessToken}`,
            },
          },
        );
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        const data = await response.json();
        setDocuments(data);
        setFilteredDocuments(data);
      } catch (error) {
        console.error("Error fetching data: ", error);
      }
    };

    fetchDocuments();
  }, [currentPage, teamId]);

  // 문서 클릭 핸들러
  const handleDocumentClick = (documentId: string) => {
    navigate(`/team/${teamId}/documents/${documentId}`);
  };

  return (
    <Container>
      <SearchInput
        type="text"
        placeholder="제목, 내용 검색"
        value={searchTerm}
        onChange={handleSearchChange}
      />
      <StyledButton>문서 작성</StyledButton>
      <DocumentContainer>
        {currentDocuments.map((doc) => (
          <DocumentItem
            key={doc.documentId}
            onClick={() => handleDocumentClick(doc.documentId)}
          >
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
    </Container>
  );
};

export default DocumentList;
