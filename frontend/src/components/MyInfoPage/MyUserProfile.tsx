import React, { useState, useEffect } from "react";
import { userState, isAuthenticatedState } from "../../state/authState";
import { User, Team } from "../../interface/interface.ts";
import axios from "axios";
import {
  UserProfileContainer,
  UserProfileTitle,
  ErrorText,
  Button,
  ButtonContainer,
  ErrorContainer,
  LinkContainer,
} from "./MyUserProfileStyled";
import { useRecoilState } from "recoil";
import axiosInstance from "../../axios";
import { Link, useNavigate } from "react-router-dom";

export default function MyUserProfile() {
  const [isAuthenticated, setIsAuthenticated] =
    useRecoilState(isAuthenticatedState);
  const [user, setUser] = useRecoilState(userState);
  const [error, setError] = useState<string>("");
  const [myTeamList, setMyTeamList] = useState<Team[]>([]);
  const navigate = useNavigate();

  //api 연결 부분
  useEffect(() => {
    const fetchProfileData = async () => {
      try {
        const response = await axiosInstance.get("/my-page");
        // setUser를 통해 Recoil 상태 업데이트
        setUser(response.data);

        const teamResponse = await axiosInstance.get(
          "/team/list?page=0&size=10&sort=createDt,asc",
        );
        setMyTeamList(teamResponse.data.content);
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

  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [passwordChangeError, setPasswordChangeError] = useState<string | null>(
    null,
  );
  const handleUpdatePassword = async () => {
    try {
      if (!currentPassword || !newPassword || !confirmPassword) {
        setPasswordChangeError("입력되지 않은 항목이 있습니다.");
        return;
      } else if (currentPassword !== currentPassword) {
        setPasswordChangeError("기존 비밀번호와 일치하지 않습니다.");
        return;
      } else if (newPassword !== confirmPassword) {
        setPasswordChangeError("비밀번호를 재확인해주세요.");
        return;
      } else if (newPassword.length < 8) {
        setPasswordChangeError("비밀번호는 최소 8자 이상이어야 합니다.");
        return;
      }

      // 비밀번호 변경
      try {
        const response = await axiosInstance.put("/member/password", {
          oldPassword: currentPassword,
          newPassword: newPassword,
          reNewPassword: confirmPassword,
        });
        if (response.status === 200) {
          setPasswordChangeError("");
          alert("비밀번호가 변경되었습니다.");

          //로그아웃 처리
          setIsAuthenticated(false);
          setUser(null);
          localStorage.clear();

          // SignIn 페이지로 이동
          navigate("/signIn");
        } else {
          setPasswordChangeError(
            "비밀번호 변경에 실패했습니다. 다시 시도해주세요.",
          );
        }
      } catch (error) {
        console.error("Error in handleUpdatePassword:", error);
        if (axios.isAxiosError(error)) {
          if (error.response?.status === 401) {
            setPasswordChangeError("토큰이 유효하지 않습니다.");
          } else if (error.response?.status === 400) {
            setPasswordChangeError(error.response.data.error);
          } else {
            setPasswordChangeError("서버 오류가 발생했습니다.");
          }
        } else {
          setPasswordChangeError("네트워크 오류가 발생했습니다.");
        }
      }
    } catch (error) {
      // Handle any other errors here
      console.error("Unexpected error:", error);
    }
  };
  return (
    <UserProfileContainer>
      <UserProfileTitle>내 프로필</UserProfileTitle>
      <LinkContainer>
        <Link to="/myTeamProfile">내 팀 프로필로 이동</Link>
      </LinkContainer>
      <br />
      {/* 231218 유나경 시작------------- */}
      <div className="overflow-x-auto">
        <table className="table">
          <tbody>
            {/* row 1 */}
            <tr>
              <th>이름</th>
              <td>{user?.name}</td>
            </tr>
            {/* row 2 */}
            <tr>
              <th>이메일 아이디</th>
              <td>{user?.email}</td>
            </tr>
            {/* row 3 */}
            <tr>
              <th>현재 비밀번호</th>
              <td>
                <label className="form-control w-full max-w-xs">
                  <input
                    type="password"
                    placeholder="현재 비밀번호"
                    className="input input-bordered w-full max-w-xs bg-white"
                    value={currentPassword}
                    onChange={(e) => setCurrentPassword(e.target.value)}
                  />
                  <div className="label">
                    <span className="label-text-alt">8자 이상</span>
                  </div>
                </label>
              </td>
            </tr>
            {/* row 4 */}
            <tr>
              <th>새 비밀번호</th>
              <td>
                <label className="form-control w-full max-w-xs">
                  <input
                    type="password"
                    placeholder="새 비밀번호"
                    className="input input-bordered w-full max-w-xs bg-white"
                    value={newPassword}
                    onChange={(e) => setNewPassword(e.target.value)}
                  />
                  <div className="label">
                    <span className="label-text-alt">8자 이상</span>
                  </div>
                </label>
              </td>
            </tr>
            {/* row 5 */}
            <tr>
              <th>새 비밀번호 확인</th>
              <td>
                <label className="form-control w-full max-w-xs">
                  <input
                    type="password"
                    placeholder="새 비밀번호"
                    className="input input-bordered w-full max-w-xs bg-white"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                  />
                  <div className="label">
                    <span className="label-text-alt">8자 이상</span>
                  </div>
                </label>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <ButtonContainer>
        <Button onClick={handleUpdatePassword}>변경하기</Button>
      </ButtonContainer>
      <ErrorContainer>
        {passwordChangeError && <ErrorText>{passwordChangeError}</ErrorText>}
      </ErrorContainer>
      <br />
      {/* 231218 유나경 끝------------- */}
    </UserProfileContainer>
  );
}
