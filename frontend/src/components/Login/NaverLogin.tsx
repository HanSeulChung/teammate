import axiosInstance from "../../axios";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

const NaverLogin: React.FC = () => {
  const navigate = useNavigate();
  const code: string | null = new URL(window.location.href).searchParams.get(
    "code",
  );
  const state: string | null = new URL(window.location.href).searchParams.get(
    "state",
  );
  console.log("code:", code);
  console.log("state:", state);
  const BASE_URL = "https://118.67.128.124:8080";

  useEffect(() => {
    const naver = async () => {
      try {
        const response = await axiosInstance.get(
          `${BASE_URL}/login/oauth2/code/naver?code=${code}&state=${state}`,
        );
        const token: string = response.headers.authorization;
        console.log(token);

        navigate("/homeView");
      } catch (error) {
        // Error handling
        console.error("Naver login error:", error);
      }
    };

    if (code) {
      naver();
    }
  }, [code, state, navigate, BASE_URL]);

  return <div>로딩페이지컴포넌트</div>;
};

export default NaverLogin;
