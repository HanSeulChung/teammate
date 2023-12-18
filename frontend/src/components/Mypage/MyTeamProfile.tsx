import React, { useState } from "react";
import { TeamProfileProps, Team } from "../../interface/interface";
import {
  TeamProfileContainer,
  TeamProfileTitle,
  ImageUploadContainer,
  NicknameContainer,
  ContainerWrapper,
  ButtonContainer,
} from "./MyTeamProfileStyled";
import profileImg from "../../assets/profileImg.png";
import MyTeamDelete from "./MyTeamDelete";
import MyTeamUpdate from "./MyTeamUpdate";

const TeamProfile: React.FC<TeamProfileProps> = ({
  selectedTeam,
  selectedImage,
  nickname,
  handleImageUpload,
  handleNicknameChange,
  handleUpdateProfile,
  teamList,
  teamId,
  accessToken,
}) => {
  const teamInfo = teamList.find((team) => team.id === selectedTeam);
  const teamIdFromSelectedTeam = teamInfo?.id || "";
  const teamName = teamInfo?.name || "";
  // const teamImage = teamInfo?.profileUrl || null;
  // const teamNickname = teamInfo?.nickname || "";
  const [selectedFileName, setSelectedFileName] = useState<string | null>(null);

  const handleFileInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const fileInput = e.target;
    const file = fileInput.files && fileInput.files[0];

    if (file) {
      setSelectedFileName(file.name);
      handleImageUpload(e);
    }
  };

  const handleDeleteTeam = () => {
    // Handle team deletion logic
    // ...
    console.log("Team deleted!");
  };
  return (
    <TeamProfileContainer>
      {selectedTeam && (
        <div>
          <TeamProfileTitle>{teamName} 프로필</TeamProfileTitle>
          <ContainerWrapper>
            <ImageUploadContainer>
              <img
                src={selectedImage || profileImg}
                alt="Selected"
                onClick={() => document.getElementById("imageUpload")?.click()}
              />
              <input
                title="imgupload"
                type="file"
                id="imageUpload"
                accept="image/*"
                onChange={handleFileInputChange}
              />
            </ImageUploadContainer>
            <NicknameContainer>
              <input
                type="text"
                placeholder="닉네임 입력"
                id="nickname"
                value={nickname}
                onChange={handleNicknameChange}
              />
            </NicknameContainer>
          </ContainerWrapper>
          <ButtonContainer>
            <MyTeamUpdate
              accessToken={accessToken}
              selectedTeam={{
                teamParticipantsId: 1,
                teamNickName: "example",
                participantsProfileUrl: "example.jpg",
              }}
              selectedImage={selectedImage}
              nickname={nickname}
              onUpdateProfile={() => {
                console.log("프로필이 업데이트되었습니다!");
              }}
            />
            <MyTeamDelete
              onDeleteTeam={handleDeleteTeam}
              teamId={teamIdFromSelectedTeam}
              accessToken={accessToken}
            />
          </ButtonContainer>
        </div>
      )}
    </TeamProfileContainer>
  );
};

export default TeamProfile;
