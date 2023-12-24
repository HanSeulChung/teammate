import { useEffect, useState } from "react";
import { useRecoilValue, useRecoilState } from "recoil";
import axiosInstance from "../../axios";
import { Link, useNavigate, useLocation } from "react-router-dom";
import styled from "styled-components";
import {
  useSearchState,
  userTeamsState,
  accessTokenState,
  isAuthenticatedState,
} from "../../state/authState";
import { Team } from "../../interface/interface.ts";

const HomeContent = () => {
  const [userTeams, setUserTeams] = useRecoilState<Team[]>(userTeamsState);
  const { search } = useSearchState();
  const [error, setError] = useState<string | null>(null);
  const accessToken = useRecoilValue(accessTokenState);
  const navigate = useNavigate();
  const location = useLocation();
  const [team, setTeam] = useState<Team | null>(location.state?.team || null);
  const [inviteCode, setInviteCode] = useState<string | null>(null);
  const isAuthenticated = useRecoilValue(isAuthenticatedState);

  useEffect(() => {
    const fetchLoggedInUserId = async () => {
      try {
        if (!isAuthenticated) {
          navigate("/signIn");
          return;
        }
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
        setError(error.message || "An error occurred");
      }
    };

    fetchLoggedInUserId();
  }, [accessToken, setUserTeams, isAuthenticated]);

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

  return (
    <TeamListContainer>
      {filteredTeamList.map((team, index) => (
        <TeamItem key={index}>
          <TeamCard
            onClick={() =>
              navigate(`/team/${team.teamId}/schedules`, {
                state: { team },
              })
            }
          >
            <TeamName>{team.name}</TeamName>
            {team.profileUrl && (
              <TeamImage src={team.profileUrl} alt={`${team.name} 이미지`} />
            )}
          </TeamCard>
        </TeamItem>
      ))}
    </TeamListContainer>
  );
};

export default HomeContent;

const TeamListContainer = styled.div`
  position: absolute;
  width: 1000px;
  top: 280px;
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
  gap: 20px;
  padding: 16px;
`;

const TeamItem = styled.li`
  list-style: none;
  width: calc(20% - 16px);
  margin-bottom: 16px;
`;

const TeamCard = styled.div`
  border: 1px solid #ccc;
  border-radius: 12px;
  padding: 12px;
  text-align: center;
  transition: transform 0.2s ease-in-out;
  height: 170px;

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
  margin: 0 auto;
  object-fit: cover;
`;
