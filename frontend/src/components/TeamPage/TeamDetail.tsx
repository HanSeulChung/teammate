import { useEffect, useState } from "react";
import axiosInstance from "../../axios";
import styled from "styled-components";
import { useParams, useLocation } from "react-router-dom";
import { useRecoilValue } from "recoil";
import { teamListState, userState } from "../../state/authState";
import { Team } from "../../interface/interface.ts";

const TeamDetail = () => {
  const { teamId } = useParams<{ teamId: string }>();
  const location = useLocation();
  console.log("Location state:", location.state); // 추가된 부분
  const [team, setTeam] = useState<Team | null>(location.state?.team || null);
  const [error, setError] = useState<string | null>(null);
  // const teamList = useRecoilValue(teamListState);

  // const team = location.state?.team as Team;

  useEffect(() => {
    // 컴포넌트가 마운트될 때만 실행되는 로직 추가
    console.log("Component is mounted");
    // useEffect(() => {
    //   const fetchTeamDetail = async () => {
    //     try {
    //       if (teamId) {
    //         const response = await axiosInstance(`/team/${teamId}`);
    //         setTeam(response.data);
    //       } else {
    //         setError("팀 ID가 없습니다.");
    //       }
    //     } catch (error) {
    //       console.error("Error fetching team detail:", error);
    //       setError("팀 정보를 가져오는 중에 오류가 발생했습니다.");
    //     }
    //   };

    //   fetchTeamDetail();
    // }, [teamId]);

    return () => {
      console.log("Component will unmount");
      // ... 추가적인 cleanup 로직 수행 ...
    };
  }, []);

  if (error) {
    return <div>Error: {error}</div>;
  }

  return (
    <TeamDetailContainer>
      {/* <TeamName>{team.name}</TeamName> */}
      <h1>{team?.name}</h1>
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
