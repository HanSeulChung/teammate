import react, { useState, useEffect } from "react";
import logo from "../../assets/logo.png";
import { Link, useLocation } from "react-router-dom";
import {
  HeaderImg,
  HeaderUl,
  HeaderTag,
  HeaderLink,
  LogoContainer,
} from "../../styles/HeaderStyled";
import { useRecoilState, useRecoilValue } from "recoil";
import { isAuthenticatedState, teamListState } from "../../state/authState";
import { useNavigate } from "react-router-dom";
import AlarmModal from "../alarm/AlarmModal";
// import Logout from "../Login/LogOut";

const Header = () => {
  const [isAuthenticated, setIsAuthenticated] =
    useRecoilState(isAuthenticatedState);
  const navigate = useNavigate();
  const location = useLocation();
  const teamList = useRecoilValue(teamListState);
  const [teamName, setTeamName] = useState("");
  const [isModalOpen, setModalOpen] = useState(false);

  const handleLogout = () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    navigate("/signin"); // 로그인 페이지로 리디렉션
    setIsAuthenticated(false);
  };

  //api버전
  // const handleLogoutSuccess = () => {
  //   // 로그아웃 성공 시 호출되는 콜백 함수
  //   setIsAuthenticated(false); // isAuthenticated 상태를 업데이트
  // };

  const isTeamPage = location.pathname.startsWith("/team/");
  const handleTeamMembersClick = () => {
    if (isTeamPage) {
      const currentPath = window.location.pathname;
      navigate(`${currentPath}/teammembers`);
    }
  };

  useEffect(() => {
    // 현재 페이지가 팀 페이지이고 팀 목록이 존재할 경우에만 팀 이름 설정
    if (isTeamPage && teamList.length > 0) {
      const teamId = location.pathname.split("/")[2];
      const team = teamList.find((team) => team.id === teamId);
      setTeamName(team ? team.name : "팀을 찾을 수 없음");
    }
  }, [location.pathname, isTeamPage, teamList]);

  const handleNotificationClick = () => {
    setModalOpen(true); // 알람 모달 열기
  };

  const closeModal = () => {
    setModalOpen(false); // 알람 모달 닫기
  };

  return (
    <HeaderTag>
      {isAuthenticated ? (
        <>
          <LogoContainer>
            <div>
              <Link to="/homeview">
                <HeaderImg src={logo} />
              </Link>
            </div>
            <div style={{ marginLeft: "10px" }}>
              {isTeamPage && <h3>{teamName}</h3>}
            </div>
          </LogoContainer>
        </>
      ) : (
        <>
          <Link to="/">
            <HeaderImg src={logo} />
          </Link>
        </>
      )}
      <nav>
        <HeaderUl>
          {isAuthenticated ? (
            <>
              <li>
                <span
                  onClick={handleLogout}
                  style={{ cursor: "pointer", color: "#333333" }}
                >
                  로그아웃
                </span>
                {/* Logout 컴포넌트 사용 
                <Link to="/signin">
                  <Logout onLogoutSuccess={() => handleLogoutSuccess()} />
                </Link>*/}
              </li>
              <li>
                {isTeamPage ? (
                  <span onClick={handleTeamMembersClick}>팀프로필</span>
                ) : (
                  <Link to="/mypage">마이페이지</Link>
                )}
              </li>
            </>
          ) : (
            <>
              <li>
                <Link to="/signin">로그인</Link>
              </li>
              <li>
                <Link to="/signup">회원가입</Link>
              </li>
              <li>
                <span onClick={handleNotificationClick}>알람</span>
                {isModalOpen && <AlarmModal closeModal={closeModal} />}{" "}
                {/* 모달 렌더링 */}
              </li>
            </>
          )}
        </HeaderUl>
      </nav>
    </HeaderTag>
  );
};

export default Header;
