import React, { useEffect, useState } from "react";
import axios from "axios";
import axiosInstance from "../../axios";

interface MyTeamUpdateProps {
  selectedTeam: {
    teamParticipantsId: number;
    teamNickName: string;
    participantsProfileUrl: string;
  } | null;
  accessToken: string;
  selectedImage: string | null;
  nickname: string;
  onUpdateProfile: () => void;
}

const MyTeamUpdate: React.FC<MyTeamUpdateProps> = ({
  selectedTeam,
  selectedImage,
  nickname,
  onUpdateProfile,
}) => {
  const [error, setError] = useState<string | null>(null);
  const [updatedNickname, setUpdatedNickname] = useState<string>(nickname);
  const [updatedImage, setUpdatedImage] = useState<string | null>(
    selectedImage,
  );
  const [updateSuccess, setUpdateSuccess] = useState<boolean>(false);

  // 프로필을 업데이트하는 함수
  const handleUpdateProfile = async () => {
    try {
      if (!selectedTeam) {
        // 선택된 팀이 없는 경우 아무 작업도 수행하지 않음
        return;
      }
      const response = await axiosInstance.patch("/member/participants", {
        teamParticipantsId: selectedTeam.teamParticipantsId,
        teamNickName: updatedNickname,
        participantsProfileUrl: updatedImage,
      });

      onUpdateProfile();
      // 업데이트 성공 메시지를 표시하기 위해 상태 변경
      setUpdateSuccess(true);
      console.log(response);
    } catch (error) {
      setError("프로필을 업데이트할 수 없습니다.");
    }
  };

  useEffect(() => {
    setUpdatedNickname(selectedTeam?.teamNickName || "");
    setUpdatedImage(selectedTeam?.participantsProfileUrl || "");
    // 업데이트 성공 시 메시지를 3초 후에 감추기 위한 로직
    if (updateSuccess) {
      const timeout = setTimeout(() => {
        setUpdateSuccess(false);
      }, 3000);
      return () => clearTimeout(timeout);
    }
  }, [selectedTeam, updateSuccess]);

  if (error) {
    return <div>{error}</div>;
  }

  return (
    <div>
      <button onClick={handleUpdateProfile}>변경하기</button>
      {updateSuccess && <div>프로필이 성공적으로 업데이트되었습니다!</div>}
    </div>
  );
};

export default MyTeamUpdate;
