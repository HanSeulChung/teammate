import React from "react";
import { Link } from "react-router-dom";
import styled from "styled-components";

const CenteredContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
`;

const CreateTeamButton = styled.button`
  margin-top: 20px;
  padding: 8px 16px;
  background-color: #a3cca3;
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  color: #333333;

  &:hover {
    background-color: #cccccc;
  }
`;

export default function HomeCreateTeamBtn() {
  return (
    <CenteredContainer>
      <Link to="/teaminfo">
        <CreateTeamButton>+ 팀 생성하기</CreateTeamButton>
      </Link>
    </CenteredContainer>
  );
}
