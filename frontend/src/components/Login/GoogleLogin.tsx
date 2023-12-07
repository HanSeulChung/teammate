import axios from "axios";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

const GoogleLogin: React.FC = () => {
  const navigate = useNavigate();
  const code: string | null = new URL(window.location.href).searchParams.get("code");
  const BASE_URL = 'http://localhost:3000';

  useEffect(() => {
    const google = async () => {
      try {
        const response = await axios.get(
          `${BASE_URL}/api/v1/members/naverLogin?code=${code}`
        );
        const token: string = response.headers.authorization;
        console.log(token);

        navigate('/');
      } catch (error) {
        // Error handling
        console.error('google login error:', error);
      }
    };

    if (code) {
      google();
    }
  }, [code,navigate, BASE_URL]);

  return (
    <div>
      로딩페이지컴포넌트
    </div>
  );
}

export default GoogleLogin;
