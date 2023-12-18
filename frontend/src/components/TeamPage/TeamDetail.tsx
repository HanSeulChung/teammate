import React, { useEffect, useState } from "react";
import axiosInstance from "../../axios";
import styled from "styled-components";
import { useParams } from "react-router-dom";
import { useRecoilValue } from "recoil";
import { teamListState, userState } from "../../state/authState";

interface TeamDetailResponse {
  teamId: number;
  teamName: string;
  code: string;
  memberLimit: number;
  inviteLink: string;
  teamImg: string;
}

const TeamDetail = () => {
  const { teamId } = useParams<{ teamId: string }>();
  console.log("Team ID:", teamId);
  const [team, setTeam] = useState<TeamDetailResponse | null>(null);
  const [error, setError] = useState<string | null>(null);
  const teamList = useRecoilValue(teamListState);

  useEffect(() => {
    const fetchTeamDetail = async () => {
      try {
        if (teamId) {
          const response = await axiosInstance.get<TeamDetailResponse>(
            `/team/${teamId}`,
          );
          setTeam(response.data);
        } else {
          // teamId가 없는 경우에 대한 처리
          console.error("팀 ID가 없습니다.");
          setError("팀 ID가 없습니다.");
        }
      } catch (error) {
        console.error("Error fetching team detail:", error);
        setError("팀 정보를 가져오는 중에 오류가 발생했습니다.");
      }
    };

    fetchTeamDetail();
  }, [teamId]);

  if (error) {
    return <div>Error: {error}</div>;
  }

  return (
    <TeamDetailContainer>
      {/* <TeamName>{team.name}</TeamName> */}
      <h1>{team?.teamName}</h1>
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
