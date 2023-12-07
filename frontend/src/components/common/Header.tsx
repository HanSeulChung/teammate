import logo from "../../assets/logo.png";
import { Link } from "react-router-dom";
import {
  HeaderImg,
  HeaderUl,
  HeaderTag,
  HeaderLink,
} from "../../styles/HeaderStyled";
import { useRecoilState } from "recoil";
import { isAuthenticatedState } from "../../state/authState";
import { useNavigate } from "react-router-dom";

const Header = () => {
  const [isAuthenticated, setIsAuthenticated] =
    useRecoilState(isAuthenticatedState);
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    navigate("/signin"); // 로그인 페이지로 리디렉션
    setIsAuthenticated(false);
  };

  return (
    <HeaderTag>
      {isAuthenticated ? (
        <>
          <Link to="/homeview">
            <HeaderImg src={logo} />
          </Link>
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
              </li>
              <li>
                <Link to="/mypage">마이페이지</Link>
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
            </>
          )}
        </HeaderUl>
      </nav>
    </HeaderTag>
  );
};

export default Header;
