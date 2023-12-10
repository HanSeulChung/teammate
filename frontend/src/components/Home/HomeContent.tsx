import React from "react";
import { useRecoilValue } from "recoil";
import { Link } from "react-router-dom";
import { teamListState, useSearchState } from "../../state/authState";

const HomeContent = () => {
  const teamList = useRecoilValue(teamListState);
  const { search } = useSearchState();

  return (
    <div>
      <h2>팀 목록</h2>
      <ul>
        {teamList
          .filter((team) => team.name.includes(search))
          .map((team, index) => (
            <li key={index}>
              <>
                <Link to={`/team/${team.id}`}>
                  <h3>{team.name}</h3>
                  {team.image ? (
                    <img
                      src={team.image}
                      alt={`${team.name} 이미지`}
                      style={{ maxWidth: "100px", maxHeight: "100px" }}
                    />
                  ) : null}
                </Link>
              </>
            </li>
          ))}
      </ul>
    </div>
  );
};

export default HomeContent;
