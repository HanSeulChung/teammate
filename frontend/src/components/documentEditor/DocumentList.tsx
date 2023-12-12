import React from "react";
import styled from "styled-components";

const DocumentContainer = styled.div`
  box-sizing: border-box;
  width: 1024px;
  height: 40rem;
  display: flex;
  align-content: space-between;
  flex-wrap: wrap;
`;

const DocumentItem = styled.div`
  width: 180px;
  height: 50%;
  border: 1px solid black;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  margin: 10px;
`;

const TitleContentContainer = styled.div``;

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
  align-self: flex-end;
`;

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
  documents: Document[];
};

const DocumentList: React.FC<DocumentListProps> = ({ documents }) => {
  return (
    <>
      <StyledButton>Add docs</StyledButton>
      <DocumentContainer>
        {documents.map((doc) => (
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
    </>
  );
};

export default DocumentList;
