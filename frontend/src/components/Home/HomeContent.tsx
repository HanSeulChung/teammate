import React from "react";
import { useRecoilValue } from "recoil";
import { teamListState } from "../../state/authState";

const HomeContent = () => {
  const teamList = useRecoilValue(teamListState);

  return (
    <div>
      <h2>팀 목록</h2>
      <ul>
        {teamList.map((team, index) => (
          <li key={index}>
            <h3>{team.name}</h3>
            {team.image && (
              <img
                src={team.image}
                alt={`${team.name} 이미지`}
                style={{ maxWidth: "100px", maxHeight: "100px" }}
              />
            )}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default HomeContent;
