import React, { useState, useEffect } from "react";
import { useRecoilState } from "recoil";
import {
  teamNameState,
  selectedTeamSizeState,
  teamListState,
} from "../../state/authState";
import { useNavigate } from "react-router-dom";
import {
  StyledContainer,
  StyledFormItem,
  StyledImagePreview,
  StyledErrorMessage,
  StyledHeading,
} from "../../styles/TeamInfoStyled";

export default function TeamInfo() {
  const [teamName, setTeamName] = useRecoilState(teamNameState);
  const [selectedTeamSize, setSelectedTeamSize] = useRecoilState(
    selectedTeamSizeState,
  );
  const [selectedImage, setSelectedImage] = useState<string | null>(null);
  const [teamList, setTeamList] = useRecoilState(teamListState);
  const [error, setError] = useState<string | null>(null); // 에러 상태 추가
  const navigate = useNavigate();

  // 페이지 로드 시 초기값 설정
  useEffect(() => {
    setTeamName("");
    setSelectedTeamSize("");
    setSelectedImage(null);
    setError(null);
  }, []);

  const handleCreateTeam = () => {
    // 입력값 확인
    let errorMessage = "";

    if (!teamName) {
      errorMessage = "팀 이름을 입력해주세요.";
    } else if (teamList.some((team) => team.name === teamName)) {
      errorMessage = "이미 있는 팀 이름입니다.";
    } else if (!selectedTeamSize) {
      errorMessage = "인원 수를 선택해주세요.";
    } else if (!selectedImage) {
      errorMessage = "이미지를 업로드해주세요.";
    }

    if (errorMessage) {
      setError(errorMessage);
      return;
    }

    setError(null); // 에러가 없다면 에러 상태 초기화
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
    <StyledContainer>
      <StyledHeading>팀 생성</StyledHeading>
      <StyledFormItem>
        <label htmlFor="teamName">팀 이름</label>
        <input
          type="text"
          id="teamName"
          value={teamName}
          onChange={(e) => {
            setTeamName(e.target.value);
            setError(null); // 팀 이름이 변경될 때 에러 상태 초기화
          }}
        />
        {/* 이미 있는 팀 이름인 경우 에러 메시지 표시 */}
        {teamList.some((team) => team.name === teamName) && (
          <div style={{ color: "red" }}>이미 있는 팀 이름입니다.</div>
        )}
      </StyledFormItem>
      <StyledFormItem>
        <label htmlFor="teamSize">인원 수</label>
        <select
          id="teamSize"
          value={selectedTeamSize}
          onChange={(e) => {
            setSelectedTeamSize(e.target.value);
            setError(null); // 인원 수 선택 시 에러 상태 초기화
          }}
        >
          <option value="" disabled>
            선택하세요
          </option>
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
      </StyledFormItem>
      {selectedImage && (
        <StyledImagePreview>
          <img src={selectedImage} alt="Selected" />
        </StyledImagePreview>
      )}
      <StyledFormItem>
        <button onClick={handleCreateTeam}>생성하기</button>
        {error && <StyledErrorMessage>{error}</StyledErrorMessage>}
      </StyledFormItem>
    </StyledContainer>
  );
}
