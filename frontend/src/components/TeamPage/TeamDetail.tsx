import React from "react";
import styled from "styled-components";
import { useParams } from "react-router-dom";
import { useRecoilValue } from "recoil";
import { teamListState, userState } from "../../state/authState";

const TeamDetail = () => {
  const { teamId } = useParams();
  const teamList = useRecoilValue(teamListState);
  const team = teamList.find((team) => team.id === teamId);

  if (!team) {
    return <div>팀을 찾을 수 없습니다.</div>;
  }

  return (
    <TeamDetailContainer>
      {/* <TeamName>{team.name}</TeamName> */}
      <BoxContainer>
        <EmptyBox>공유문서리스트</EmptyBox>
        <EmptyBox>캘린더</EmptyBox>
      </BoxContainer>

      {/* {team.image && <Image src={team.image} alt={`${team.name} 이미지`} />} */}
    </TeamDetailContainer>
  );
};

export default TeamDetail;

const TeamDetailContainer = styled.div`
  text-align: center;
`;

const EmptyBox = styled.div`
  flex: 1;
  border: 2px solid #333;
  height: 700px;
  border-radius: 30px;
  margin: 20px;
`;

const BoxContainer = styled.div`
  display: flex;
  margin-top: 20px;
`;

