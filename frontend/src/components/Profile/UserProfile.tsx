// UserProfile.js

import React from "react";
import { User, Team } from "../../state/authState"; // User 타입을 import

interface UserProfileProps {
  user: User | null;
  teamList: Team[];
  selectedTeam: string | null;
  handleTeamSelect: (event: React.ChangeEvent<HTMLSelectElement>) => void;
}

const UserProfile: React.FC<UserProfileProps> = ({
  user,
  teamList,
  selectedTeam,
  handleTeamSelect,
}) => {
  return (
    <div>
      <h2>내 프로필</h2>
      {user && (
        <div>
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
        </div>
      )}
    </div>
  );
};

export default UserProfile;
