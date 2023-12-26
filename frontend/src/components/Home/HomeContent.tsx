import { useEffect, useState } from "react";
import { useRecoilValue, useRecoilState } from "recoil";
import axiosInstance from "../../axios";
import { useNavigate } from "react-router-dom";
import styled from "styled-components";
import {
  searchState,
  userTeamsState,
  accessTokenState,
  isAuthenticatedState,
} from "../../state/authState";
import { Team } from "../../interface/interface.ts";

const ITEMS_PER_PAGE = 10;

const HomeContent = () => {
  const [userTeams, setUserTeams] = useRecoilState<Team[]>(userTeamsState);
  const search = useRecoilValue(searchState);
  const [error, setError] = useState<string | null>(null);
  const accessToken = useRecoilValue(accessTokenState);
  const navigate = useNavigate();
  const isAuthenticated = useRecoilValue(isAuthenticatedState);
  const [currentPage, setCurrentPage] = useState(1);

  useEffect(() => {
    const fetchLoggedInUserId = async () => {
      try {
        if (!isAuthenticated) {
          navigate("/signIn");
          return;
        }
        const teamListResponse = await axiosInstance.get("/team/list");
        setUserTeams(teamListResponse.data);
        window.sessionStorage.setItem(
          `teamList_${accessToken}`,
          JSON.stringify(teamListResponse.data),
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

  const filteredTeamList = userTeams
    .filter((team) => team.name.toLowerCase().includes(search.toLowerCase()))
    .filter((team) => {
      return (
        team.restorationDt === null ||
        team.teamRole === "READER" ||
        (team.teamRole === "MATE" && team.restorationDt === null)
      );
    });

  const indexOfLastTeam = currentPage * ITEMS_PER_PAGE;
  const indexOfFirstTeam = indexOfLastTeam - ITEMS_PER_PAGE;
  const currentTeams = filteredTeamList.slice(
    indexOfFirstTeam,
    indexOfLastTeam,
  );

  const paginate = (pageNumber: number) => {
    setCurrentPage(pageNumber);
  };

  const handleRestoration = async (teamId: number) => {
    try {
      await axiosInstance.patch(`/team/{teamId}/restore?restoreDt=2023-01-01`);
      const updatedTeamList = await axiosInstance.get("/team/list");
      setUserTeams(updatedTeamList.data);
    } catch (error: any) {
      console.error("팀 복구 중 에러 발생:", error);
    }
  };

  return (
    <TeamListContainer>
      {currentTeams.map((team, index) => (
        <TeamItem key={index}>
          <TeamCard
            onClick={() => {
              if (team.restorationDt !== null) {
                return;
              }
              navigate(`/team/${team.teamId}`, {
                state: { team },
              });
            }}
          >
            <TeamName>{team.name}</TeamName>
            {team.profileUrl && (
              <TeamImage src={team.profileUrl} alt={`${team.name} 이미지`} />
            )}
            {team.restorationDt !== null && (
              <RestorationButton onClick={() => handleRestoration(team.teamId)}>
                복구
              </RestorationButton>
            )}
          </TeamCard>
        </TeamItem>
      ))}
      <Pagination>
        {Array.from({
          length: Math.ceil(filteredTeamList.length / ITEMS_PER_PAGE),
        }).map((_, index) => (
          <PageNumber key={index} onClick={() => paginate(index + 1)}>
            {index + 1}
          </PageNumber>
        ))}
      </Pagination>
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

const Pagination = styled.div`
  display: flex;
  justify-content: center;
  width: 100%;
  height: 30px;
`;

const PageNumber = styled.div`
  width: 30px;
  height: 30px;
  cursor: pointer;
  color: #333333;
  border-radius: 50px;
  text-align: center;
  padding: 4px;

  &:hover {
    background-color: #a3cca3;
  }
`;

const RestorationButton = styled.button`
  background-color: #a3cca3;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  margin-top: 8px;
  cursor: pointer;

  &:hover {
    background-color: #cccccc;
  }
`;
