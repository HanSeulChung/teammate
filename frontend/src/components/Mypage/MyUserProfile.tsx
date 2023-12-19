import React, { useState, useEffect } from "react";
import { userState } from "../../state/authState";
import { User, Team, UserProfileProps } from "../../interface/interface";
import axios from "axios";
import {
  UserProfileContainer,
  UserProfileTitle,
  UserProfileInfo,
  Email,
  UpdateButton,
} from "./MyUserProfileStyled";
import PasswordChangeModal from "./PasswordChangeModal";
import { useRecoilState } from "recoil";
import axiosInstance from "../../axios";

const UserProfile: React.FC<UserProfileProps> = ({
  teamList,
  selectedTeam,
  handleTeamSelect,
}) => {
  const [user, setUser] = useRecoilState(userState);
  const [isPasswordChangeModalOpen, setIsPasswordChangeModalOpen] =
    useState(false);
  const [error, setError] = useState<string>("");

  //api 연결 부분
  useEffect(() => {
    const fetchProfileData = async () => {
      try {
        const response = await axiosInstance.get("/mypage");
        // setUser를 통해 Recoil 상태 업데이트
        setUser(response.data);
      } catch (error) {
        console.error("Error fetching profile data:", error);

        if (axios.isAxiosError(error)) {
          console.error("Axios Error Response:", error.response);
          if (error.response?.status === 401) {
            setError("토큰이 유효하지 않습니다. 다시 로그인해주세요.");
          } else if (error.response?.status === 403) {
            setError(
              "토큰에 해당하는 사용자 정보를 가져오는 중에 문제가 발생했습니다.",
            );
          } else {
            setError("프로필 정보를 가져오는 중에 문제가 발생했습니다.");
          }
        } else {
          setError("프로필 정보를 가져오는 중에 문제가 발생했습니다.");
        }
      }
    };

    fetchProfileData();
  }, []);

  const handleOpenPasswordChangeModal = () => {
    setIsPasswordChangeModalOpen(true);
  };

  const handleClosePasswordChangeModal = () => {
    setIsPasswordChangeModalOpen(false);
  };
  return (
    <UserProfileContainer>
      <UserProfileTitle>내 프로필</UserProfileTitle>
      <br />
      {user && (
        <UserProfileInfo>
          <p>이름: {user.name}</p>
          <span>
            <Email>Email: {user.id}</Email>
            <UpdateButton onClick={handleOpenPasswordChangeModal}>
              비밀번호 변경
            </UpdateButton>
          </span>
          <select
            title="myteam"
            id="teamSelect"
            value={selectedTeam || ""}
            onChange={handleTeamSelect}
          >
            <option value="" disabled>
              팀을 선택하세요
            </option>
            {teamList.map((team) => (
              <option key={team.teamId} value={team.teamId}>
                {team.name}
              </option>
            ))}
          </select>
        </UserProfileInfo>
      )}
      {isPasswordChangeModalOpen && (
        <PasswordChangeModal onClose={handleClosePasswordChangeModal} />
      )}
    </UserProfileContainer>
  );
};

export default UserProfile;
