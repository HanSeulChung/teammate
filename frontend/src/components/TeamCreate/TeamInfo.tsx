// TeamInfo.tsx

import React, { useState } from "react";
import { useRecoilState } from "recoil";
import {
  teamNameState,
  selectedTeamSizeState,
  teamListState,
} from "../../state/authState";
import { useNavigate } from "react-router-dom";

export default function TeamInfo() {
  const [teamName, setTeamName] = useRecoilState(teamNameState);
  const [selectedTeamSize, setSelectedTeamSize] = useRecoilState(
    selectedTeamSizeState,
  );
  const [selectedImage, setSelectedImage] = useState<string | null>(null);
  const [teamList, setTeamList] = useRecoilState(teamListState);
  const navigate = useNavigate();

  const handleCreateTeam = () => {
    // TODO: 팀 생성 로직을 구현하세요.
    // teamName, selectedTeamSize, selectedImage를 이용하여 팀을 생성하는 로직을 작성합니다.
    console.log("팀 생성 로직 수행");
    console.log("팀 이름:", teamName);
    console.log("인원 수:", selectedTeamSize);
    console.log("선택된 이미지:", selectedImage);

    // 생성한 팀 정보를 Recoil 상태에 추가
    const newTeam = {
      name: teamName,
      size: selectedTeamSize,
      image: selectedImage,
    };

    setTeamList([...teamList, newTeam]);

    // 이동
    navigate("/homeview");
  };

  const handleImageUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];

    if (file) {
      // 이미지 파일을 선택한 경우
      const reader = new FileReader();
      reader.readAsDataURL(file);

      reader.onload = () => {
        const result = reader.result as string;
        setSelectedImage(result);
      };
    }
  };

  return (
    <div>
      <h2>팀 생성</h2>
      <div>
        <label htmlFor="teamName">팀 이름</label>
        <input
          type="text"
          id="teamName"
          value={teamName}
          onChange={(e) => setTeamName(e.target.value)}
        />
      </div>
      <div style={{ display: "flex", alignItems: "center" }}>
        <label htmlFor="teamSize">인원 수</label>
        <select
          id="teamSize"
          value={selectedTeamSize}
          onChange={(e) => setSelectedTeamSize(e.target.value)}
        >
          <option value="1-9">1~9명</option>
          <option value="10-99">10~99명</option>
          <option value="100+">100명 이상</option>
        </select>
        <input
          placeholder="이미지 업로드"
          type="file"
          id="imageUpload"
          accept="image/*"
          onChange={handleImageUpload}
        />
      </div>
      {selectedImage && (
        <div>
          <img
            src={selectedImage}
            alt="Selected"
            style={{ maxWidth: "100px", maxHeight: "100px" }}
          />
        </div>
      )}
      <div>
        <button onClick={handleCreateTeam}>생성하기</button>
      </div>
    </div>
  );
}
