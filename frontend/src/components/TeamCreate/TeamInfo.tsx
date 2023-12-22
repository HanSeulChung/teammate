import React, { useState, useEffect } from "react";
import { useRecoilState, useRecoilValue } from "recoil";
import axios from "axios";
import axiosInstance from "../../axios";
import {
  teamNameState,
  selectedTeamSizeState,
  teamListState,
  userState,
  accessTokenState,
} from "../../state/authState";
import { useNavigate } from "react-router-dom";
import {
  StyledContainer,
  StyledFormItem,
  StyledErrorMessage,
  StyledHeading,
  StyledLabel,
  StyledInput,
  ImageUploadContainer,
  StyledButton,
} from "./TeamInfoStyled";
import profileImg from "../../assets/profileImg.png";
import { TeamInfoData } from "../../interface/interface";

export default function TeamInfo() {
  const [teamName, setTeamName] = useRecoilState(teamNameState);
  const [selectedTeamSize, setSelectedTeamSize] = useRecoilState(
    selectedTeamSizeState,
  );
  const [selectedImage, setSelectedImage] = useState<File | null>(null);
  const [previewImage, setPreviewImage] = useState<string | null>(null);
  const [teamList, setTeamList] = useRecoilState(teamListState);
  const [error, setError] = useState<string | null>(null);
  const user = useRecoilValue(userState);
  const navigate = useNavigate();
  const accessToken = useRecoilValue(accessTokenState);

  const handleFileInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const fileInput = e.target;
    const file = fileInput.files && fileInput.files[0];

    if (file) {
      const reader = new FileReader();
      reader.readAsDataURL(file);

      reader.onload = () => {
        const result = reader.result as string;
        setSelectedImage(file); // 파일 경로(string)를 저장
        setPreviewImage(result);
        console.log("Selected Image:", result);
      };

      reader.onerror = (error) => {
        console.error("Error reading the file:", error);
      };
    }
  };
  useEffect(() => {
    setTeamName("");
    setSelectedTeamSize("");
    setSelectedImage(null);
    setError(null);
  }, []);

  const handleCreateTeam = async () => {
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

    setError(null);

    try {
      let memberLimit;

      if (selectedTeamSize === "1-9") {
        memberLimit = 9;
      } else if (selectedTeamSize === "10-99") {
        memberLimit = 99;
      } else if (selectedTeamSize === "100+") {
        memberLimit = Infinity;
      } else {
        setError("올바르지 않은 teamSize 값입니다.");
        return;
      }

      const formData = new FormData();
      formData.append("teamName", teamName);
      if (selectedImage instanceof File) {
        formData.append("teamImg", selectedImage);
      }
      formData.append("memberLimit", memberLimit.toString());
      const response = await axiosInstance.post("/team", formData, {
        withCredentials: true,
        headers: {
          Authorization: `Bearer ${accessToken}`,
          "Content-Type": "multipart/form-data",
        },
      });
      alert(`팀 프로필에서 초대코드를 확인하세요!`);
      navigate("/homeView");
      // 성공적으로 팀 생성이 완료되었을 때의 로직
      console.log("팀 생성 성공:", response.data);
    } catch (error) {
      // 에러가 발생했을 때의 로직
      console.error("팀 생성 중 오류:", error);

      if (axios.isAxiosError(error)) {
        console.error("Axios Error Response:", error.response);
      }
      const axiosError = error as any;
      if (axiosError.response?.status === 401) {
        setError("토큰이 유효하지 않습니다.");
      } else {
        setError("팀 생성 중 오류가 발생했습니다.");
      }
    }
  };

  return (
    <StyledContainer>
      <StyledHeading>팀 생성</StyledHeading>
      <StyledFormItem>
        <StyledLabel htmlFor="teamName">팀 이름</StyledLabel>
        <StyledInput
          type="text"
          id="teamName"
          value={teamName}
          placeholder="팀 이름"
          onChange={(e) => {
            setTeamName(e.target.value);
            setError(null);
          }}
        />
        {teamList.some((team) => team.name === teamName) && (
          <StyledErrorMessage>이미 있는 팀 이름입니다.</StyledErrorMessage>
        )}
      </StyledFormItem>
      <StyledFormItem>
        <StyledLabel htmlFor="teamSize">인원 수</StyledLabel>
        <select
          title="teamSize"
          id="teamSize"
          value={selectedTeamSize}
          onChange={(e) => {
            setSelectedTeamSize(e.target.value);
            setError(null);
          }}
        >
          <option value="" disabled>
            인원 수
          </option>
          <option value="1-9">1~9명</option>
          <option value="10-99">10~99명</option>
          <option value="100+">100명 이상</option>
        </select>
        <ImageUploadContainer>
          <img
            src={
              previewImage ||
              (typeof selectedImage === "string" ? selectedImage : profileImg)
            }
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
      </StyledFormItem>
      <StyledFormItem>
        <StyledButton onClick={handleCreateTeam}>생성하기</StyledButton>
        {error && <StyledErrorMessage>{error}</StyledErrorMessage>}
      </StyledFormItem>
    </StyledContainer>
  );
}
