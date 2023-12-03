import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom'; // useNavigate 추가
import MockAdapter from 'axios-mock-adapter';

const mock = new MockAdapter(axios); // MockAdapter 인스턴스 생성

const SignIn = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const navigate = useNavigate(); // useNavigate 초기화

  // '/sign-in' 엔드포인트에 대한 POST 요청을 가로채고 가짜 응답을 설정
  mock.onPost('/sign-in').reply(200, {
    accessToken: '가짜AccessToken',
    refreshToken: '가짜RefreshToken',
  });

  const handleSignIn = async () => {
    try {
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
    // 회원가입 페이지로 이동
    navigate('/signup');
  };

  return (
    <div>
      <h2>로그인</h2>
      <div>
        <label>Email:</label>
        <input type="email" title="email" value={email} onChange={(e) => setEmail(e.target.value)} />
      </div>
      <div>
        <label>Password:</label>
        <input type="password" title="password" value={password} onChange={(e) => setPassword(e.target.value)} />
      </div>
      <button onClick={handleSignIn}>로그인</button>
      <p>
        계정이 없으신가요?{' '}
        <span style={{ cursor: 'pointer', color: 'blue' }} onClick={handleSignUp}>
          회원가입
        </span>
      </p>
      {error && <p style={{ color: 'red' }}>{error}</p>}
    </div>
  );
};

export default SignIn;