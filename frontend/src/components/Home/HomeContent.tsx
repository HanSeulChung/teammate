import React,{useEffect} from "react";
import { useRecoilValue,useRecoilState } from "recoil";
import { Link } from "react-router-dom";
import styled from "styled-components";
import { teamListState, useSearchState,userTeamsState, initialTeams} from "../../state/authState";

const HomeContent = () => {
  const teamList = useRecoilValue(teamListState);
  const { search } = useSearchState();
  const userTeams = useRecoilState(userTeamsState)[0];

  useEffect(() => {
    localStorage.setItem("teamList", JSON.stringify(teamList));
  }, [teamList]);

//  // 현재 로그인된 사용자가 속한 팀만 필터링
//  const userTeams = teamList.filter((team) =>
//  team.members.some((member) => member.id === loggedInUserId)
// );
if (!Array.isArray(userTeams)) {
  // userTeams가 배열이 아닌 경우에 대한 처리
  return null;
}
const filteredTeamList = userTeams.filter((team) =>
 team.name.toLowerCase().includes(search.toLowerCase())
);
  return (
    <TeamListContainer>
      {filteredTeamList.map((team, index) => (
        <TeamItem key={index}>
          <TeamLink to={`/team/${team.id}`}>
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