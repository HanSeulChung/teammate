import React, { useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const KakaoLogin = () => {
  const navigate = useNavigate();
  const code: string | null = new URL(window.location.href).searchParams.get('code');
  const BASE_URL = '백엔드한테 달라하자3'// 백에서 받기

  useEffect(() => {
    const kakao = async () => {
      try {
        const response = await axios.get(`${BASE_URL}/api/v1/members/kakaoLogin?code=${code}`);
        const token = response.headers.authorization;
        
        // 여기서 쿠키를 어떻게 설정하고 있는지 확인이 필요합니다.
        // 아래는 예시일 뿐 실제 사용하는 방법에 맞게 수정이 필요합니다.
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
