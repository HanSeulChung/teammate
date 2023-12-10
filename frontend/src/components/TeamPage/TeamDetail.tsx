// TeamDetail.js
import React from "react";
import { useParams } from "react-router-dom";
import { useRecoilValue } from "recoil";
import { teamListState } from "../../state/authState";

const TeamDetail = () => {
  const { teamId } = useParams();
  const teamList = useRecoilValue(teamListState);
  const team = teamList.find((team) => team.id === teamId);

  if (!team) {
    // 팀이 없는 경우에 대한 처리
    return <div>팀을 찾을 수 없습니다.</div>;
  }

  return (
    <div>
      <h2>{team.name}</h2>
      {team.image && (
        <img
          src={team.image}
          alt={`${team.name} 이미지`}
          style={{ maxWidth: "200px", maxHeight: "200px" }}
        />
      )}
      {/* 나머지 팀 세부 정보를 표시 */}
    </div>
  );
};

export default TeamDetail;
