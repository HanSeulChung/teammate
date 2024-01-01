import axios, { AxiosInstance, AxiosError } from "axios";
import { saveAccessToken, saveRefreshToken } from "./state/authState"; // 필요에 따라 import 경로를 업데이트하세요.

const axiosInstance: AxiosInstance = axios.create({
  baseURL: "https://www.teammate.digital:8080",
  withCredentials: true,
  headers: {
    "Content-Type": "application/json",
  },
});

axiosInstance.interceptors.request.use(
  (config) => {
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
    return response;
  },
  async (error: AxiosError) => {
    if (axios.isAxiosError(error)) {
      const originalConfig = error.config;
      if (originalConfig) {
        const refreshToken = window.sessionStorage.getItem("refreshToken");
        if (refreshToken) {
          try {
            const response = await axios.post(
              "http://localhost:8080/refresh-token",
              { refreshToken },
              { withCredentials: true },
            );

            const newAccessToken = response.data.accessToken;
            const newRefreshToken = response.data.refreshToken;

            originalConfig.headers.Authorization = `Bearer ${newAccessToken}`;
            saveAccessToken(newAccessToken);
            saveRefreshToken(newRefreshToken);

            return axiosInstance(originalConfig);
          } catch (refreshError) {
            console.error("토큰 갱신 중 오류:", refreshError);
            return Promise.reject(refreshError);
          }
        }
      }
    }

    return Promise.reject(error);
  },
);

export default axiosInstance;
