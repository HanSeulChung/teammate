import React, { useEffect, useState } from "react";
import { useRecoilValue, useRecoilState } from "recoil";
import axiosInstance from "../../axios";
import { Link } from "react-router-dom";
import styled from "styled-components";
import {
  teamListState,
  useSearchState,
  userTeamsState,
  accessTokenState,
} from "../../state/authState";
import { Team } from "../../interface/interface";

const HomeContent = () => {
  const [userTeams, setUserTeams] = useRecoilState<Team[]>(userTeamsState);
  const { search } = useSearchState();
  // const navigate = useNavigate();
  const [error, setError] = useState<string | null>(null);
  const accessToken = useRecoilValue(accessTokenState);

  useEffect(() => {
    const fetchLoggedInUserId = async () => {
      try {
        const teamListResponse = await axiosInstance.get("/team/list", {
          params: { page: 0, size: 10, sort: "createDt-asc" },
        });
        // Recoil 상태 업데이트
        setUserTeams(teamListResponse.data.content);

        // localStorage에 저장
        localStorage.setItem(
          `teamList_${accessToken}`,
          JSON.stringify(teamListResponse.data.content),
        );
      } catch (error: any) {
        console.error("Error fetching team list:", error);
      }
    };

    fetchLoggedInUserId();
  }, [accessToken, setUserTeams]);

  if (error) {
    return <div>{error}</div>;
  }

  // userTeams가 배열이 아닌 경우에 대한 처리
  if (!Array.isArray(userTeams)) {
    return null;
  }

  const filteredTeamList = userTeams.filter((team) =>
    team.name.toLowerCase().includes(search.toLowerCase()),
  );
  // useEffect(() => {
  //   const loggedInUserId = getLoggedInUserId();
  //   // 이 부분에서 localStorage에서 teamList를 불러와서 userTeams에 설정합니다.
  //   const storedTeamList = localStorage.getItem(`teamList_${loggedInUserId}`);
  //   if (storedTeamList) {
  //     setUserTeams(JSON.parse(storedTeamList));
  //   }
  // }, [setUserTeams]);

  return (
    <TeamListContainer>
      {filteredTeamList.map((team, index) => (
        <TeamItem key={index}>
          <TeamLink to={`/team/${team.teamId}`}>
            <TeamCard>
              <TeamName>{team.name}</TeamName>
              {team.image && (
                <TeamImage src={team.image} alt={`${team.name} 이미지`} />
              )}
            </TeamCard>
          </TeamLink>
        </TeamItem>
      ))}
    </TeamListContainer>
  );
};

export default HomeContent;

const TeamListContainer = styled.div`
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
  gap: 16px;
  padding: 16px;
`;

const TeamItem = styled.li`
  list-style: none;
  width: calc(20% - 16px);
  margin-bottom: 16px;
`;

const TeamLink = styled(Link)`
  text-decoration: none;
  color: #333;
`;

const TeamCard = styled.div`
  border: 1px solid #ccc;
  border-radius: 12px;
  padding: 12px;
  text-align: center;
  transition: transform 0.2s ease-in-out;

  &:hover {
    transform: scale(1.05);
    color: #a3cca3;
  }
`;

const TeamName = styled.h3`
  margin-bottom: 8px;
`;

const TeamImage = styled.img`
  max-width: 100%;
  max-height: 100px;
  border-radius: 8px;
  margin-bottom: 8px;
`;
