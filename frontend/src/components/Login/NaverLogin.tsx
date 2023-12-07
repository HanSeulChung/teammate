import axios from "axios";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

const NaverLogin: React.FC = () => {
  const navigate = useNavigate();
  const code: string | null = new URL(window.location.href).searchParams.get("code");
  const state: string | null = new URL(window.location.href).searchParams.get("state");

  const BASE_URL = 'http://localhost:3000';

  useEffect(() => {
    const naver = async () => {
      try {
        const response = await axios.get(
          `${BASE_URL}/api/v1/members/naverLogin?code=${code}&state=${state}`
        );
        const token: string = response.headers.authorization;
        console.log(token);

        navigate('/');
      } catch (error) {
        // Error handling
        console.error('Naver login error:', error);
      }
    };

    if (code) {
      naver();
    }
  }, [code, state, navigate, BASE_URL]);

  return (
    <div>
      로딩페이지컴포넌트
    </div>
  );
}

export default NaverLogin;
