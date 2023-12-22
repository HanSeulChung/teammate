import { useState } from "react";
import axios, { AxiosError } from "axios";
import axiosInstance from "../../axios";
import { useNavigate } from "react-router-dom";
import { StyledContainer, StyledFormItem } from "./SignInStyled";
import { useRecoilState } from "recoil";
import {
  isAuthenticatedState,
  accessTokenState,
  refreshTokenState,
  saveAccessToken,
  saveRefreshToken,
  useUser,
} from "../../state/authState";

const SignIn = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [isAuthenticated, setIsAuthenticated] =
    useRecoilState(isAuthenticatedState);
  const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
  const [refreshToken, setRefreshToken] = useRecoilState(refreshTokenState);
  const { saveUser } = useUser();
  const navigate = useNavigate();

  const handleSignIn = async () => {
    if (!email.trim() || !password.trim()) {
      setError("입력되지 않은 항목이 있습니다.");
      return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email.trim())) {
      setError("올바른 이메일 형식이 아닙니다.");
      return;
    }

    try {
      if (password.length < 8) {
        setError("비밀번호는 최소 8자 이상이어야 합니다.");
        return;
      }
      const response = await axiosInstance.post("/sign-in", {
        email: email,
        password: password,
      });

      const { token } = response.data;
      axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
      console.log("토큰이 발급되었습니다:", response.data.token);
      const newAccessToken = response.data.accessToken;
      const newRefreshToken = response.data.refreshToken;

      if (newAccessToken && newRefreshToken) {
        // 토큰 저장
        saveAccessToken(newAccessToken);
        saveRefreshToken(newRefreshToken);

        // Recoil 상태 업데이트
        setAccessToken(newAccessToken);
        setRefreshToken(newRefreshToken);
      }
      setIsAuthenticated(true);
      saveUser({ id: email, name: response.data.name });
      navigate("/homeview");
      console.log("login successful");
    } catch (error) {
      console.error("Sign In Error:", error);

      if (axios.isAxiosError(error)) {
        console.error("Axios Error Response:", error.response);
      }

      const axiosError = error as AxiosError<any>;
      if (axiosError.response?.status === 400) {
        const errorCode = axiosError.response.data.errorCode;
        let errorMessage = "";

        if (errorCode === "PASSWORD_NOT_MATCH_EXCEPTION") {
          errorMessage = "비밀번호가 일치하지 않습니다. 다시 입력해주세요.";
        } else if (errorCode === "EMAIL_NOT_MATCH_EXCEPTION") {
          errorMessage = "이메일이 일치하지 않습니다. 다시 입력해주세요.";
        } else if (errorCode === "EMAIL_NOT_FOUND_EXCEPTION") {
          errorMessage = "이메일로 가입된 사용자가 없습니다.";
        }

        setError(errorMessage);
      } else {
        setError("이메일 인증 후 로그인 해주세요.");
      }
    }
  };

  const handleLogout = () => {
    saveAccessToken("");
    saveRefreshToken("");
    navigate("/signin");
    setIsAuthenticated(false);
  };

  const handleSignUp = () => {
    navigate("/signup");
  };

  return (
    <>
      <StyledContainer>
        <h2>로그인</h2>
        <br />
        <StyledFormItem>
          <input
            type="email"
            title="email"
            value={email}
            placeholder="이메일"
            onChange={(e) => setEmail(e.target.value)}
          />
        </StyledFormItem>
        <br />
        <StyledFormItem>
          <input
            type="password"
            title="password"
            placeholder="비밀번호"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </StyledFormItem>
        <br />
        <StyledFormItem>
          {isAuthenticated ? (
            <button onClick={handleLogout}>로그아웃</button>
          ) : (
            <button onClick={handleSignIn}>로그인</button>
          )}
        </StyledFormItem>
        <p>
          계정이 없으신가요?{" "}
          <span
            style={{ cursor: "pointer", color: "#333333" }}
            onClick={handleSignUp}
          >
            회원가입
          </span>
        </p>
        {error && <p style={{ color: "red" }}>{error}</p>}
      </StyledContainer>
    </>
  );
};

export default SignIn;
