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
  width: 35%;
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
  span {
    margin-bottom: 15px;
    display: flex;
    justify-content: space-between;
  }

  label {
    margin-right: 10px;
  }

  select {
    padding: 5px;
    outline: none;
    border: none;
    border-bottom: 1px solid #333333;
    border-radius: 0;
  }
`;
const Email = styled.span`
  flex: 1.3;
  margin: 10px 0;
`;

const UpdateButton = styled.button`
  flex: 0.7;
  margin-left: 20px;
  padding: 0;
  background-color: #cccccc;
  color: #333333;
  cursor: pointer;
  border: none;
  border-radius: 20px;

  &:hover {
    background-color: #a3cca3;
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
          <span>
            <Email>Email: {user.id}</Email>
            <UpdateButton>비밀번호 변경</UpdateButton>
          </span>
          <select
            title="myteam"
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
