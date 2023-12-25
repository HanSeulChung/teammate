import axios, { AxiosInstance, AxiosError } from "axios";
import { saveAccessToken, saveRefreshToken } from "./state/authState"; // 필요에 따라 import 경로를 업데이트하세요.

const axiosInstance: AxiosInstance = axios.create({
  baseURL: "http://118.67.128.124:8080", // 기본 URL을 업데이트하세요.
  withCredentials: true,
  headers: {
    "Content-Type": "application/json",
  },
});

axiosInstance.interceptors.request.use(
  (config) => {
    //토큰이 있는지 확인하고, 있다면 헤더에 추가합니다.
    const accessToken = window.sessionStorage.getItem("accessToken");
    if (accessToken) {
      config.headers["Authorization"] = `Bearer ${accessToken}`;
    }

    return config;
  },
  (error: AxiosError) => {
    return Promise.reject(error);
  },
);

axiosInstance.interceptors.response.use(
  (response) => {
    // 응답 성공 시 로직
    return response;
  },
  async (error: AxiosError) => {
    // 응답 오류 시 로직
    if (axios.isAxiosError(error)) {
      // 토큰 갱신 로직
      const originalConfig = error.config;
      if (originalConfig) {
        const refreshToken = window.sessionStorage.getItem("refreshToken");
        if (refreshToken) {
          try {
            // 새로운 토큰 요청
            const response = await axios.post(
              "http://118.67.128.124:8080/refresh-token",
              { refreshToken },
              { withCredentials: true },
            );

            const newAccessToken = response.data.accessToken;
            const newRefreshToken = response.data.refreshToken;

            // 새로 발급된 토큰으로 기존 요청 재시도
            originalConfig.headers.Authorization = `Bearer ${newAccessToken}`;
            saveAccessToken(newAccessToken);
            saveRefreshToken(newRefreshToken);

            return axiosInstance(originalConfig);
          } catch (refreshError) {
            // 토큰 갱신에 실패하면 로그인 페이지로 이동 또는 다른 로직 수행
            console.error("토큰 갱신 중 오류:", refreshError);
            // navigate('/signIn'); // 예시로 로그인 페이지로 이동
            return Promise.reject(refreshError);
          }
        }
      }
    }

    // 토큰 갱신 시도 중 오류가 발생한 경우 또는 다른 오류 처리
    return Promise.reject(error);
  },
);

export default axiosInstance;
