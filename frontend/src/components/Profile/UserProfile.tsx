// UserProfile.js

import React from "react";
import { User, Team } from "../../state/authState"; // User 타입을 import
import styled from "styled-components";

interface UserProfileProps {
  user: User | null;
  teamList: Team[];
  selectedTeam: string | null;
  handleTeamSelect: (event: React.ChangeEvent<HTMLSelectElement>) => void;
}

const UserProfileContainer = styled.div`
  padding: 20px;
  border-radius: 8px;
  margin: auto;
  width: 25%;
`;

const UserProfileTitle = styled.h2`
  font-size: 1.5rem;
  margin-bottom: 10px;
  text-align: center;
`;

const UserProfileInfo = styled.div`
  p {
    margin: 0;
    margin-bottom: 15px;
    text-align: left;
  }

  label {
    margin-right: 10px;
  }

  select {
    margin-left: 10px;
    padding: 5px;
  }
`;

const UserProfile: React.FC<UserProfileProps> = ({
  user,
  teamList,
  selectedTeam,
  handleTeamSelect,
}) => {
  return (
    <UserProfileContainer>
      <UserProfileTitle>내 프로필</UserProfileTitle>
      <br />
      {user && (
        <UserProfileInfo>
          <p>이름: {user.name}</p>
          <p>Email: {user.id}</p>
          <label htmlFor="teamSelect">소속 팀 선택:</label>
          <select
            id="teamSelect"
            value={selectedTeam || ""}
            onChange={handleTeamSelect}
          >
            <option value="" disabled>
              팀을 선택하세요
            </option>
            {teamList.map((team) => (
              <option key={team.name} value={team.name}>
                {team.name}
              </option>
            ))}
          </select>
        </UserProfileInfo>
      )}
    </UserProfileContainer>
  );
};

export default UserProfile;
