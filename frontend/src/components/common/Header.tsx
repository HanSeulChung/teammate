import { useState } from "react";
import logo from "../../assets/logo.png";
import { Link, useLocation } from "react-router-dom";
import { HeaderImg, LogoContainer, Div, Span } from "../../styles/HeaderStyled";
import { useRecoilState } from "recoil";
import { isAuthenticatedState, userState } from "../../state/authState";
import { useNavigate } from "react-router-dom";
import AlarmModal from "../alarm/AlarmModal";
import axiosInstance from "../../axios";
import axios, { AxiosError } from "axios";

const Header = () => {
  const [isAuthenticated, setIsAuthenticated] =
    useRecoilState(isAuthenticatedState);
  const navigate = useNavigate();
  const location = useLocation();
  const [teamName] = useState("");
  const [isModalOpen, setModalOpen] = useState(false);
  const [, setUser] = useRecoilState(userState);
  const isTeamPage = location.pathname.startsWith("/team/");

  const handleTeamProfileClick = async () => {
    if (isTeamPage) {
      const teamIdMatch = location.pathname.match(/\/team\/(\d+)/);
      if (teamIdMatch) {
        const teamId = parseInt(teamIdMatch[1]);

        try {
          const response = await axiosInstance.get(
            `/team/${teamId}/participant`,
          );
          const userTeamRole = response.data.teamRole;

          if (userTeamRole === "LEADER") {
            navigate(`/team/${teamId}/teamLeader`);
          } else if (userTeamRole === "MATE") {
            navigate(`/team/${teamId}/teamMembers`);
          } else {
          }
        } catch (error) {
          console.error("Error fetching user team role:", error);
        }
      }
    }
  };

  const onLogoutSuccess = () => {
    console.log("로그아웃 성공!");
  };

  //로그아웃
  const handleLogout = async () => {
    try {
      await axiosInstance.post("/logout");
      window.sessionStorage.clear();
      console.log("로컬 스토리지가 비워졌습니다.");
      console.log("로그아웃 되었습니다.");
      setIsAuthenticated(false);
      setUser(null);
      onLogoutSuccess();

      navigate("/");
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

  const handleNotificationClick = () => {
    setModalOpen(true);
  };

  const closeModal = () => {
    setModalOpen(false);
  };

  return (
    <header className="navbar bg-white">
      <div className="flex-1">
        {isAuthenticated ? (
          <>
            <LogoContainer>
              <div>
                <Link to="/homeView">
                  <HeaderImg src={logo} />
                </Link>
              </div>
              <Div>{isTeamPage && <h3>{teamName}</h3>}</Div>
            </LogoContainer>
          </>
        ) : (
          <>
            <Link to="/">
              <HeaderImg src={logo} />
            </Link>
          </>
        )}
      </div>
      <div className="flex-none">
        <ul className="menu menu-horizontal px-1">
          {isAuthenticated ? (
            <>
              <li>
                <Span onClick={handleLogout}>로그아웃</Span>
              </li>
              <li>
                {isTeamPage ? (
                  <span onClick={handleTeamProfileClick}>팀프로필</span>
                ) : (
                  <Link to="/myUserProfile">마이페이지</Link>
                )}
              </li>
              <li>
                <button
                  className="btn btn-ghost btn-circle"
                  onClick={handleNotificationClick}
                  aria-label="알림 보기"
                >
                  <div className="indicator">
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      className="h-5 w-5"
                      fill="none"
                      viewBox="0 0 24 24"
                      stroke="currentColor"
                    >
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth="2"
                        d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"
                      />
                    </svg>
                    <span className="badge badge-xs badge-primary indicator-item"></span>
                  </div>
                </button>
                {isModalOpen && <AlarmModal closeModal={closeModal} />}{" "}
              </li>
            </>
          ) : (
            <>
              <li>
                <Link to="/signIn">로그인</Link>
              </li>
              <li>
                <Link to="/signUp">회원가입</Link>
              </li>
            </>
          )}
        </ul>
      </div>
    </header>
  );
};

export default Header;
