import React from "react";
import axios, { AxiosError } from "axios";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../../axios";

interface LogoutProps {
  onLogoutSuccess: () => void;
}

const Logout: React.FC<LogoutProps> = ({ onLogoutSuccess }) => {
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      // 로그아웃 API 호출
      await axiosInstance.post("/logout");

      window.sessionStorage.clear();
      console.log("로그아웃 되었습니다.");
      onLogoutSuccess();

      // 로그아웃 후 페이지 이동
      navigate("/signIn");
    } catch (error: AxiosError | any) {
      if (axios.isAxiosError(error)) {
        console.error("네트워크 에러:", error.message);
      } else if (error.response) {
        console.error(
          "서버 응답 에러:",
          error.response.status,
          error.response.data,
        );
      } else {
        console.error("요청 설정 에러:", error.message);
      }
    }
  };

  return (
    <div>
      {/* 로그아웃 버튼 클릭 시 handleLogout 함수 호출 */}
      <button onClick={handleLogout}>로그아웃</button>
    </div>
  );
};

export default Logout;
