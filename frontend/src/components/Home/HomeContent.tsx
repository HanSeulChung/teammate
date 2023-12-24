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
        const [teamListResponse, inviteCodeResponse] = await Promise.all([
          axiosInstance.get("/team/list", {
            params: { page: 0, size: 10, sort: "createDt-asc" },
          }),
          team && axiosInstance.post(`/team/${team.teamId}/${team.code}`),
        ]);

        // Recoil 상태 업데이트
        setUserTeams(teamListResponse.data.content);

        // localStorage에 저장
        localStorage.setItem(
          `teamList_${accessToken}`,
          JSON.stringify(teamListResponse.data.content),
        );

        //초대 url을 통해 들어온 사용자에게 팀 추가 (중복 방지)
        if (team && inviteCodeResponse) {
          const newTeam: Team = {
            teamId: inviteCodeResponse.data.teamId,
            code: inviteCodeResponse.data.code,
            name: team.name,
            profileUrl: team.profileUrl,
            leaderId: null,
            nickname: null,
            members: [],
            memberLimit: team.memberLimit,
            inviteLink: team.inviteLink,
          };

          // 새로운 팀을 현재 유저의 팀 리스트에 추가
          setUserTeams((prevTeams) => [...prevTeams, newTeam]);
        }
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
              navigate(`/team/${team.teamId}`, {
                state: { team },
              })
            }
          >
            <TeamName>{team.name}</TeamName>
            {team.profileUrl && (
              <TeamImage src={team.profileUrl} alt={`${team.name} 이미지`} />
            )}
          </TeamCard>
          {/* </TeamLink> */}
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
  margin: 0 auto;
`;
