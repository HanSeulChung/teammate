import { useEffect } from "react";
import { useRecoilState } from "recoil";
import { useNavigate } from "react-router-dom";
import {
  accessTokenState,
  refreshTokenState,
  saveAccessToken,
  saveRefreshToken,
} from "../../state/authState";
import axios from "axios";

const SocialServiceCallbackPage = () => {
  const [, setAccessToken] = useRecoilState(accessTokenState);
  const [, setRefreshToken] = useRecoilState(refreshTokenState);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      const urlParams = new URLSearchParams(window.location.search);
      const newAccessToken = urlParams.get("access_token");
      const newRefreshToken = urlParams.get("refresh_token");

      try {
        const result = await axios.get(`/social-success/`);

        console.log(result);
      } catch (error) {
        console.log("error", error);
      }

      if (newAccessToken && newRefreshToken) {
        saveAccessToken(newAccessToken);
        saveRefreshToken(newRefreshToken);

        navigate("/homeView");
        console.log("login successful");
      }
    };
    fetchData();
  }, [navigate, setAccessToken, setRefreshToken]);

  return <div>로딩페이지컴포넌트</div>;
};

export default SocialServiceCallbackPage;
