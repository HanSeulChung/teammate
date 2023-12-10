import React, { useState, useEffect } from "react";
import { useUser, teamListState } from "../../state/authState";
import { useRecoilValue } from "recoil";
import UserProfile from "./MyUserProfile";
import TeamProfile from "./MyTeamProfile";

const Mypage = () => {
  const { user } = useUser();
  const teamList = useRecoilValue(teamListState);
  const [selectedTeam, setSelectedTeam] = useState<string | null>(null);
  const [nickname, setNickname] = useState<string>("");
  const [selectedImage, setSelectedImage] = useState<string | null>(null);

  // 팀이 변경될 때마다 해당 팀의 이미지와 닉네임 초기화
  useEffect(() => {
    setSelectedImage(null);
    setNickname("");
  }, [selectedTeam]);

  const handleTeamSelect = (event: React.ChangeEvent<HTMLSelectElement>) => {
    const selectedTeamName = event.target.value;
    setSelectedTeam(selectedTeamName);

    // 팀이 변경될 때 해당 팀의 이미지와 닉네임 초기화
    setSelectedImage(null);
    setNickname("");
  };

  const handleImageUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];

    if (file) {
      const reader = new FileReader();
      reader.readAsDataURL(file);

      reader.onload = () => {
        const result = reader.result as string;
        setSelectedImage(result);
      };
    }
  };

  const handleNicknameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNickname(e.target.value);
  };

  const handleUpdateProfile = (
    image: string | null,
    updatedNickname: string,
  ) => {
    // 프로필을 업데이트하는 로직 추가
    console.log("프로필 업데이트:", image, updatedNickname);
  };

  return (
    <div>
      <UserProfile
        user={user}
        teamList={teamList}
        selectedTeam={selectedTeam}
        handleTeamSelect={handleTeamSelect}
      />
      <TeamProfile
        selectedTeam={selectedTeam}
        selectedImage={selectedImage}
        nickname={nickname}
        handleImageUpload={handleImageUpload}
        handleNicknameChange={handleNicknameChange}
        handleUpdateProfile={handleUpdateProfile}
      />
    </div>
  );
};

export default Mypage;
