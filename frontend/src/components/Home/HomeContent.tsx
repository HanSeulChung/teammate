import React from "react";
import { useRecoilValue } from "recoil";
import { teamListState } from "../../state/authState";
import { useSearchState } from "../../state/authState";

const HomeContent = () => {
  const teamList = useRecoilValue(teamListState);
  const { search } = useSearchState();

  return (
    <div>
      <h2>팀 목록</h2>
      <ul>
        {teamList
          .filter((team) => team.name.includes(search)) // 여기에서 검색어와 일치하는 경우에만 필터링
          .map((team, index) => (
            <li key={index}>
              <>
                <h3>{team.name}</h3>
                {team.image ? (
                  <img
                    src={team.image}
                    alt={`${team.name} 이미지`}
                    style={{ maxWidth: "100px", maxHeight: "100px" }}
                  />
                ) : null}
              </>
            </li>
          ))}
      </ul>
    </div>
  );
};

export default HomeContent;
