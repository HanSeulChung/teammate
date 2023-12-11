import React, { useState } from "react";
import {
  TeamProfileContainer,
  TeamProfileTitle,
  ImageUploadContainer,
  NicknameContainer,
  UpdateButton,
  ContainerWrapper,
  ButtonContainer,
  DeleteButton,
} from "./MyTeamProfileStyled";
import profileImg from "../../assets/profileImg.png";

interface TeamProfileProps {
  selectedTeam: string | null;
  selectedImage: string | null;
  nickname: string;
  handleImageUpload: (e: React.ChangeEvent<HTMLInputElement>) => void;
  handleNicknameChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  handleUpdateProfile: (image: string | null, nickname: string) => void;
}

const TeamProfile: React.FC<TeamProfileProps> = ({
  selectedTeam,
  selectedImage,
  nickname,
  handleImageUpload,
  handleNicknameChange,
  handleUpdateProfile,
}) => {
  const [selectedFileName, setSelectedFileName] = useState<string | null>(null);

  const handleFileInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const fileInput = e.target;
    const file = fileInput.files && fileInput.files[0];

    if (file) {
      setSelectedFileName(file.name);
      handleImageUpload(e);
    }
  };
  return (
    <TeamProfileContainer>
      {selectedTeam && (
        <div>
          <TeamProfileTitle>{selectedTeam} 프로필</TeamProfileTitle>
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
            <UpdateButton
              onClick={() => handleUpdateProfile(selectedImage, nickname)}
            >
              변경하기
            </UpdateButton>
            <DeleteButton>탈퇴하기</DeleteButton>
          </ButtonContainer>
        </div>
      )}
    </TeamProfileContainer>
  );
};

export default TeamProfile;
