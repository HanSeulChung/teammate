import React,{useEffect} from "react";
import { useRecoilValue } from "recoil";
import { Link } from "react-router-dom";
import styled from "styled-components";
import { teamListState, useSearchState } from "../../state/authState";

const HomeContent = () => {
  const teamList = useRecoilValue(teamListState);
  const { search } = useSearchState();

  useEffect(() => {
    localStorage.setItem("teamList", JSON.stringify(teamList));
  }, [teamList]);

  const filteredTeamList = teamList.filter((team) =>
    team.name.toLowerCase().includes(search.toLowerCase()),
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
