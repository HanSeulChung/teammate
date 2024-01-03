import { useEffect } from "react";
import { useRecoilState } from "recoil";
import { useNavigate } from "react-router-dom";
import {
  isAuthenticatedState,
  accessTokenState,
  refreshTokenState,
  saveAccessToken,
  saveRefreshToken,
} from "../../state/authState";
import axios from "axios";

const SocialServiceCallbackPage = () => {
  const [, setAccessToken] = useRecoilState(accessTokenState);
  const [, setRefreshToken] = useRecoilState(refreshTokenState);
  const [, setIsAuthenticated] = useRecoilState(isAuthenticatedState);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      const urlParams = new URLSearchParams(window.location.search);
      const newAccessToken = urlParams.get("access_token");
      const newRefreshToken = urlParams.get("refresh_token");

      try {
        await axios.get(`/social-success/`);
      } catch (error) {
        console.log("error", error);
      }

      if (newAccessToken && newRefreshToken) {
        saveAccessToken(newAccessToken);
        saveRefreshToken(newRefreshToken);
        setIsAuthenticated(true);
        navigate("/homeView");
      }
    };
    fetchData();
  }, [navigate, setAccessToken, setRefreshToken]);

  return <div>로그인 중입니다.</div>;
};

export default SocialServiceCallbackPage;
