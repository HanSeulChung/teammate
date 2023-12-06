import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { StyledContainer, StyledFormItem } from "../styles/SignInStyled";
import { useRecoilState } from "recoil";
import { isAuthenticatedState } from "../state/authState";

const SignIn = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [isAuthenticated, setIsAuthenticated] =
    useRecoilState(isAuthenticatedState);
  const navigate = useNavigate();

  // Mock users data for testing
  const mockUsers = [
    { id: "user1@example.com", password: "password1" },
    { id: "user2@example.com", password: "password2" },
  ];

  const handleLogout = () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    navigate("/signin");
    setIsAuthenticated(false);
  };

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
      const user = mockUsers.find(
        (u) => u.id === email && u.password === password,
      );

      if (user) {
        const accessToken = "가짜AccessToken";
        const refreshToken = "가짜RefreshToken";

        setIsAuthenticated(true);

        navigate("/main");
      } else {
        setError("올바른 이메일 또는 비밀번호를 입력하세요.");
      }
    } catch (error) {
      console.error(error);
    }
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
