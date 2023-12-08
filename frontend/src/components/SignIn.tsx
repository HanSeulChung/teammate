import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import MockAdapter from 'axios-mock-adapter';
import { StyledContainer, StyledFormItem } from '../styles/SignInStyled'; 

const mock = new MockAdapter(axios);

const SignIn = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const navigate = useNavigate();

  mock.onPost('/sign-in').reply(200, {
    accessToken: '가짜AccessToken',
    refreshToken: '가짜RefreshToken',
  });

  const handleSignIn = async () => {
    // 입력 유효성 검사
    if (!email.trim() || !password.trim()) {
      setError('입력되지 않은 항목이 있습니다.');
      return;
    }

    // 이메일 유효성 검사
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email.trim())) {
      setError('올바른 이메일 형식이 아닙니다.');
      return;
    }

    try {
      // 비밀번호 안전성 검사 추가
      if (password.length < 8) {
        setError('비밀번호는 최소 8자 이상이어야 합니다.');
        return;
      }

      const response = await axios.post('/sign-in', {
        id: email,
        password: password,
      });

      const { accessToken, refreshToken } = response.data;
      console.log('accessToken:', accessToken);
      console.log('refreshToken:', refreshToken);

    } catch (error) {
      if (axios.isAxiosError(error)) {
        if (error.response) {
          setError(error.response.data.message);
        } else if (error.request) {
          setError('서버 응답이 없습니다.');
        } else {
          setError('요청을 보내는 중 에러가 발생했습니다.');
        }
      } else {
        setError('알 수 없는 에러가 발생했습니다.');
      }
    }
  };

  const handleSignUp = () => {
    navigate('/signup');
  };

  return (
    <StyledContainer>
      <h2>로그인</h2><br/>
      <StyledFormItem>
        <input type="email" title="email" value={email} placeholder="이메일" onChange={(e) => setEmail(e.target.value)} />
      </StyledFormItem>
      <br/>
      <StyledFormItem>
        <input type="password" title="password" placeholder="비밀번호" value={password} onChange={(e) => setPassword(e.target.value)} />
      </StyledFormItem>
      <br/>
      <StyledFormItem>
        <button onClick={handleSignIn}>로그인</button>
      </StyledFormItem>
      <p>
        계정이 없으신가요?{' '}
        <span style={{ cursor: 'pointer', color:'#333333'}} onClick={handleSignUp}>
          회원가입
        </span>
      </p>
      {error && <p style={{ color: 'red' }}>{error}</p>}
    </StyledContainer>
  );
};

export default SignIn;
