import React from "react";
import axios, { AxiosError } from "axios";
import { useRecoilState, useRecoilValue } from "recoil";
import {
  selectedTeamState,
  teamListState,
  accessTokenState,
} from "../../state/authState";

interface MyTeamDeleteProps {
  onDeleteTeam: () => void;
  teamId: string;
  accessToken: string;
}

const MyTeamDelete: React.FC<MyTeamDeleteProps> = ({ onDeleteTeam }) => {
  const [selectedTeam, setSelectedTeam] = useRecoilState<string | null>(
    selectedTeamState,
  );
  const [teamList, setTeamList] = useRecoilState(teamListState);
  const accessToken = useRecoilValue(accessTokenState);

  const handleDeleteTeam = async () => {
    // if (!selectedTeam) {
    //   // 선택된 팀이 없는 경우 아무 작업도 수행하지 않음
    //   return;
    // }
    try {
      // API 호출
      const response = await axios.delete(`/team/${selectedTeam}/participant`, {
        withCredentials: true,
        headers: {
          accessToken: accessToken,
        },
      });

      // 서버 응답이 성공이면 로컬 상태 업데이트 및 콜백 호출
      const updatedTeamList = teamList.filter(
        (team) => team.id !== selectedTeam,
      );
      setTeamList(updatedTeamList);
      setSelectedTeam(null);
      onDeleteTeam();

      // 성공 메시지
      console.log(response.data);
    } catch (error: AxiosError | any) {
      // 에러 처리
      console.error("탈퇴 실패:", error.response?.data || error.message);
    }
  };

  return (
    <div>
      <button onClick={handleDeleteTeam}>팀 탈퇴하기</button>
    </div>
  );
};

export default MyTeamDelete;
