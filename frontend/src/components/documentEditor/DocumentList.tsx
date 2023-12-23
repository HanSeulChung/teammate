import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import styled from "styled-components";
import axiosInstance from "../../axios";

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
  width: 800px;
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
  width: 800px;
  min-height: 800px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
`;

const InputAndButton = styled.div`
  display: flex;
  align-items: center;
`;

const ButtonContainer = styled.div`
  width: 100%;
  display: flex;
  justify-content: space-between;
`;

const SearchInput = styled.input`
  background-color: white;
  width: 100%;
  height: 45px;
  color: black;
  font-size: 16px;
  border: 1px solid black;
  border-radius: 8px;
  padding: 12px;
`;

const TitleDomStyled = styled.h1`
  font-size: 24px;
  margin-bottom: 12px;
  font-weight: 700;
`;

const PagenationButton = styled.button`
  background-color: rgb(163, 204, 163);
`;

const PagenationButtonContainer = styled.div`
  display: flex;
  justify-content: center;
  margin-bottom: 24px;
`;

type Document = {
  id: string;
  title: string;
  content: string;
  teamId: string;
  commentsId: string[];
  createdDt: string;
  updatedDt: string;
};

type DocumentListProps = {
  teamId: number;
};

const DocumentList: React.FC<DocumentListProps> = ({ teamId }) => {
  const [documents, setDocuments] = useState<Document[]>([]);
  const [filteredDocuments, setFilteredDocuments] = useState<Document[]>([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(0);
  const pageSize = 10;
  const [Id, setId] = useState<number>(teamId);
  const API_BASE_URL = "http://118.67.128.124:8080";

  useEffect(() => {
    const fetchDocuments = async () => {
      try {
        const response = await axiosInstance.get(
          `${API_BASE_URL}/team/${Id}/documents`,
        );

        console.log(response);

        if (response.data && Array.isArray(response.data.content)) {
          const sortedDocuments = response.data.content.sort(
            (
              a: { createdDt: string | number | Date },
              b: { createdDt: string | number | Date },
            ) => {
              return (
                new Date(b.createdDt).getTime() -
                new Date(a.createdDt).getTime()
              );
            },
          );
          setDocuments(sortedDocuments);
          setFilteredDocuments(sortedDocuments);
        } else {
          // 응답에 'content' 배열이 없는 경우
          console.error("Invalid response structure:", response.data);
        }
      } catch (error) {
        console.error("Error fetching data: ", error);
      }
    };

    if (teamId) {
      fetchDocuments();
    }
  }, [teamId]);

  useEffect(() => {
    // 검색 필터링
    const filtered = documents.filter(
      (doc) =>
        doc.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
        doc.content.toLowerCase().includes(searchTerm.toLowerCase()),
    );
    setFilteredDocuments(filtered);
  }, [searchTerm, documents]);

  const handlePageChange = (page: number) => {
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
        <PagenationButton key={i} onClick={() => handlePageChange(i)}>
          {i + 1}
        </PagenationButton>,
      );
    }
    return <PagenationButtonContainer>{pages}</PagenationButtonContainer>;
  };

  const currentDocuments = filteredDocuments.slice(
    currentPage * pageSize,
    (currentPage + 1) * pageSize,
  );

  const navigate = useNavigate();

  const handleDocumentClick = (id: string) => {
    navigate(`/team/${teamId}/documents/${id}`);
  };

  const handleDocumentCreate = () => {
    navigate(`/team/${teamId}/documents`);
  };

  const handleCalendarClick = () => {
    navigate(`/team/${teamId}/schedules`);
  };

  const formatDate = (dateString: string | number | Date) => {
    const date = new Date(dateString);
    return date.toLocaleDateString();
  };

  return (
    <Container>
      <InputAndButton>
        <SearchInput
          type="text"
          placeholder="제목, 내용 검색"
          value={searchTerm}
          onChange={handleSearchChange}
        />
        <ButtonContainer>
          <StyledButton onClick={handleDocumentCreate}>문서 작성</StyledButton>
          <StyledButton onClick={handleCalendarClick}>캘린더</StyledButton>
        </ButtonContainer>
      </InputAndButton>
      <DocumentContainer>
        {currentDocuments.length !== 0 ? (
          currentDocuments.map((doc) => (
            <DocumentItem
              key={doc.id}
              onClick={() => handleDocumentClick(doc.id)}
            >
              <TitleContentContainer>
                <TitleDomStyled>제목 : {doc.title}</TitleDomStyled>
                <p>
                  내용 :{" "}
                  {doc.content.length < 20
                    ? doc.content
                    : doc.content.slice(0, 20) + "..."}
                </p>
              </TitleContentContainer>
              <DatesContainer>
                <TitleDaytime>
                  Created: {formatDate(doc.createdDt)}
                </TitleDaytime>
                <TitleDaytime>
                  Updated: {formatDate(doc.updatedDt)}
                </TitleDaytime>
              </DatesContainer>
            </DocumentItem>
          ))
        ) : (
          <span>문서가 없습니다.</span>
        )}
      </DocumentContainer>
      {renderPagination()}
    </Container>
  );
};

export default DocumentList;
