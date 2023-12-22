import React, { useEffect, useState } from "react";
import axios from "axios";
import axiosInstance from "../../axios";
import {
  TeamProfileContainer,
  TeamProfileTitle,
  ImageUploadContainer,
  NicknameContainer,
  ContainerWrapper,
  ButtonContainer,
  UserProfileInfo,
} from "./MyTeamProfileStyled";
import profileImg from "../../assets/profileImg.png";
import { Team, TeamProfileUpdate } from "../../interface/interface.ts";
import { accessTokenState } from "../../state/authState";
import { useRecoilValue } from "recoil";

const MyTeamProfile: React.FC = () => {
  const [teamList, setTeamList] = useState<Team[]>([]);
  const [selectedTeam, setSelectedTeam] = useState<Team | null>(null);
  const [selectedImage, setSelectedImage] = useState<string | null>(null);
  const [previewImage, setPreviewImage] = useState<string | null>(null);
  const [newSelectedImage, setNewSelectedImage] = useState<File | null>(null);
  const [nickName, setNickName] = useState("");
  const [userTeamData, setUserTeamData] = useState<{
    teamParticipantsId?: number;
    participantsProfileUrl?: string;
    teamNickName?: string;
  } | null>(null);
  const accessToken = useRecoilValue(accessTokenState);

  // 팀 리스트 API 호출
  useEffect(() => {
    const fetchTeamList = async () => {
      try {
        const response = await axiosInstance.get(
          "/team/list?page=0&size=10&sort=createDt,asc",
        );
        setTeamList(response.data.content);
      } catch (error) {
        console.error("Error fetching team list:", error);
      }
    };

    fetchTeamList();
  }, [accessToken]);

  //사용자 정보 api호출
  useEffect(() => {
    const fetchTeamData = async () => {
      try {
        const participantsResponse = await axiosInstance.get(
          "/member/participants",
        );

        const teamListResponse = await axiosInstance.get(
          "/team/list?page=0&size=10&sort=createDt,asc",
        );

        const teamListMap: Record<number, TeamProfileUpdate> =
          teamListResponse.data.content.reduce(
            (
              acc: Record<number, TeamProfileUpdate>,
              team: TeamProfileUpdate,
            ) => {
              acc[team.teamId] = team;
              return acc;
            },
            {},
          );

        const selectedTeamData = selectedTeam
          ? participantsResponse.data[selectedTeam.teamId]
          : undefined;

        if (selectedTeamData) {
          const teamInfo = teamListMap[selectedTeamData.teamId];

          if (teamInfo) {
            setUserTeamData({
              teamParticipantsId: selectedTeamData.teamParticipantsId,
              participantsProfileUrl: selectedTeamData.participantsProfileUrl,
              teamNickName: selectedTeamData.teamNickname,
            });
          }
        } else {
          console.error("선택된 팀의 데이터를 찾을 수 없습니다.");
        }
      } catch (error) {
        console.error("팀 데이터를 불러오는 중 에러:", error);
        // 에러 핸들링
      }
    };

    // 사용자 정보 api 호출
    fetchTeamData();
  }, [selectedTeam]);

  //프로필 수정
  const handleCreateTeam = async () => {
    const formData = new FormData();
    formData.append("teamNickName", nickName);
    formData.append(
      "teamParticipantsId",
      userTeamData?.teamParticipantsId?.toString() || "",
    );
    if (newSelectedImage instanceof File) {
      formData.append("participantImg", newSelectedImage);
    }
    console.log("FormData:", newSelectedImage);
    console.log("userTeamData", userTeamData);
    const response = await axiosInstance.post(
      "/member/participants",
      formData,
      {
        withCredentials: true,
        headers: {
          Authorization: `Bearer ${accessToken}`,
          "Content-Type": "multipart/form-data",
        },
      },
    );
    console.log("내 프로필 수정 성공 :", response.data);
  };

  //이미지업로드
  const handleFileInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const fileInput = e.target;
    const file = fileInput.files && fileInput.files[0];

    if (file) {
      const reader = new FileReader();
      reader.readAsDataURL(file);

      reader.onload = () => {
        const result = reader.result as string;
        setNewSelectedImage(file); // 파일 경로(string)를 저장
        setPreviewImage(result);
        setSelectedImage(result);
        console.log("Selected Image:", result);
      };

      reader.onerror = (error) => {
        console.error("Error reading the file:", error);
      };
    }
  };

  const handleTeamClick = (team: Team) => {
    setSelectedTeam(team);
    if (userTeamData) {
      setNickName(userTeamData.teamNickName || "");
      setSelectedImage(userTeamData.participantsProfileUrl || "");
    } else {
      setSelectedImage(profileImg);
    }
  };

  return (
    <TeamProfileContainer>
      <UserProfileInfo>
        <h2>내 팀 프로필</h2>
        <select
          title="profile"
          onChange={(e) =>
            handleTeamClick(
              teamList.find((team) => team.name === e.target.value)!,
            )
          }
        >
          <option value="">조회할 팀을 선택하세요.</option>
          {teamList.map((team) => (
            <option key={team.teamId} value={team.name}>
              {team.name}
            </option>
          ))}
        </select>
      </UserProfileInfo>
      {selectedTeam && (
        <div>
          <TeamProfileTitle>{selectedTeam.name} 프로필</TeamProfileTitle>
          <ContainerWrapper>
            <ImageUploadContainer>
              <img
                alt="profileImg"
                src={
                  typeof selectedImage === "string"
                    ? selectedImage
                    : previewImage ||
                      userTeamData?.participantsProfileUrl ||
                      profileImg
                }
                onClick={() => document.getElementById("imageUpload")?.click()}
              />
              <input
                title="imgUpload"
                type="file"
                id="imageUpload"
                accept="image/*"
                onChange={handleFileInputChange}
              />
            </ImageUploadContainer>
            <NicknameContainer>
              <input
                title="nickName"
                type="text"
                placeholder="닉네임 입력"
                id="nickname"
                value={nickName || userTeamData?.teamNickName || ""}
                onChange={(e) => setNickName(e.target.value)}
              />
            </NicknameContainer>
          </ContainerWrapper>
          <ButtonContainer>
            <button onClick={handleCreateTeam}>변경하기</button>
          </ButtonContainer>
        </div>
      )}
    </TeamProfileContainer>
  );
};

export default MyTeamProfile;
