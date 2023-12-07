import React from "react";

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
  return (
    <div>
      {selectedTeam && (
        <div>
          <h3>{selectedTeam} 팀 프로필</h3>
          <div>
            <label htmlFor="imageUpload">이미지 업로드:</label>
            <input
              type="file"
              id="imageUpload"
              accept="image/*"
              onChange={handleImageUpload}
            />
            {selectedImage && (
              <div>
                <img
                  src={selectedImage}
                  alt="Selected"
                  style={{ maxWidth: "100px", maxHeight: "100px" }}
                />
              </div>
            )}
          </div>
          <div>
            <label htmlFor="nickname">닉네임 입력:</label>
            <input
              type="text"
              id="nickname"
              value={nickname}
              onChange={handleNicknameChange}
            />
            <button
              onClick={() => handleUpdateProfile(selectedImage, nickname)}
            >
              변경 하기
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default TeamProfile;
