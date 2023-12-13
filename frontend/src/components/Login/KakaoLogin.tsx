import React, { useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const KakaoLogin: React.FC = () => {
  const navigate = useNavigate();
  const code: string | null = new URL(window.location.href).searchParams.get('code');
  const BASE_URL = 'http://localhost:3000';

  useEffect(() => {
    const kakao = async () => {
      try {
        const response = await axios.get(`${BASE_URL}/api/v1/members/kakaoLogin?code=${code}`);
        const token: string = response.headers.authorization;

        document.cookie = `token=${token}; path=/`;

        navigate('/');
      } catch (error) {
        // 오류 처리
        console.error('Kakao login error:', error);
      }
    };

    if (code) {
      kakao();
    }
  }, [code, navigate, BASE_URL]);

  return (
    <div>
      로딩 페이지 컴포넌트
    </div>
  );
};

export default KakaoLogin;
