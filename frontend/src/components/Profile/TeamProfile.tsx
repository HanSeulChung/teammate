import React from "react";
import styled from "styled-components";

interface TeamProfileProps {
  selectedTeam: string | null;
  selectedImage: string | null;
  nickname: string;
  handleImageUpload: (e: React.ChangeEvent<HTMLInputElement>) => void;
  handleNicknameChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  handleUpdateProfile: (image: string | null, nickname: string) => void;
}

const TeamProfileContainer = styled.div`
  padding: 20px;
  border-radius: 8px;
  margin: auto;
  width: 25%;
`;

const TeamProfileTitle = styled.h3`
  font-size: 1.5rem;
  margin-bottom: 10px;
  text-align: center;
  color: #333;
`;

const ImageUploadContainer = styled.div`
  margin-bottom: 15px;

  input {
    margin-bottom: 10px;
  }

  img {
    max-width: 100px;
    max-height: 100px;
  }
`;

const NicknameContainer = styled.div`
  input {
    margin-bottom: 10px;
    padding: 5px;
  }
`;
const UpdateButton = styled.button`
  flex: 1;
  margin-left: 10px;
  margin: 0 auto;
  padding: 8px;
  background-color: #a3cca3;
  color: #333333;
  cursor: pointer;

  &:hover {
    background-color: #cccccc;
  }
`;

const TeamProfile: React.FC<TeamProfileProps> = ({
  selectedTeam,
  selectedImage,
  nickname,
  handleImageUpload,
  handleNicknameChange,
  handleUpdateProfile,
}) => {
  return (
    <TeamProfileContainer>
      {selectedTeam && (
        <div>
          <TeamProfileTitle>{selectedTeam} 팀 프로필</TeamProfileTitle>
          <br />
          <ImageUploadContainer>
            <input
              title="imgupload"
              type="file"
              id="imageUpload"
              accept="image/*"
              onChange={handleImageUpload}
            />
            {selectedImage && (
              <div>
                <img src={selectedImage} alt="Selected" />
              </div>
            )}
          </ImageUploadContainer>
          <NicknameContainer>
            <input
              type="text"
              placeholder="닉네임 입력"
              id="nickname"
              value={nickname}
              onChange={handleNicknameChange}
            />
            <UpdateButton
              onClick={() => handleUpdateProfile(selectedImage, nickname)}
            >
              변경 하기
            </UpdateButton>
          </NicknameContainer>
        </div>
      )}
    </TeamProfileContainer>
  );
};

export default TeamProfile;
