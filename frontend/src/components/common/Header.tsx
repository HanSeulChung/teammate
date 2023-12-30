import { useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { useRecoilState } from "recoil";
import { isAuthenticatedState, userState } from "../../state/authState";
import axios, { AxiosError } from "axios";
import axiosInstance from "../../axios";
import styled from "styled-components";
import logo from "../../assets/logo.png";

const Header = () => {
  const [isAuthenticated, setIsAuthenticated] =
    useRecoilState(isAuthenticatedState);
  const navigate = useNavigate();
  const location = useLocation();
  const [teamName] = useState("");
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
        } catch (error) {}
      }
    }
  };

  //로그아웃
  const handleLogout = async () => {
    try {
      await axiosInstance.post("/logout");
      window.sessionStorage.clear();
      setIsAuthenticated(false);
      setUser(null);
      navigate("/");
    } catch (error: AxiosError | any) {
      if (axios.isAxiosError(error)) {
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
    <header className="navbar bg-white">
      <GnbBodyDiv>
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
      </GnbBodyDiv>
    </header>
  );
};

export default Header;

// 스타일드 컴포넌트

export const LogoContainer = styled.div`
  display: flex;
  align-items: center;
  // justify-content: center;
`;

// 헤더 로고 이미지
export const HeaderImg = styled.img`
  width: 50px;
  height: 50px;
  // margin: 15px 10px 10px 10px;
`;

export const Div = styled.div`
  margin-left: 10px;
`;
export const Span = styled.span`
  cursor: pointer;
  color: #333333;
`;

export const GnbBodyDiv = styled.div`
  width: 100%;
  max-width: 1024px;
  height: 100%;
  margin: 0 auto;
`;
