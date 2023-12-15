// import React from "react";
// import axios, { AxiosError } from "axios";
// import { useRecoilState } from "recoil";
// import { accessTokenState } from "../../state/authState";
// import { useNavigate } from "react-router-dom";

// interface LogoutProps {
//   onLogoutSuccess: () => void;
// }

// const Logout: React.FC<LogoutProps> = ({ onLogoutSuccess }) => {
//   const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
//   const navigate = useNavigate();

//   const handleLogout = async () => {
//     try {
//       // 로그아웃 API 호출
//       await axios.post("http://localhost:8080/logout", null, {
//         headers: {
//           Authorization: `Bearer ${accessToken}`,
//         },
//       });

//       //  setAccessToken(null); accessToken 초기화

//       console.log("로그아웃 되었습니다.");
//       onLogoutSuccess();

//       // 로그아웃 후 페이지 이동
//       navigate("/signin");
//     } catch (error: AxiosError | any) {
//       if (axios.isAxiosError(error)) {
//         console.error("네트워크 에러:", error.message);
//       } else if (error.response) {
//         console.error(
//           "서버 응답 에러:",
//           error.response.status,
//           error.response.data,
//         );
//       } else {
//         console.error("요청 설정 에러:", error.message);
//       }
//     }
//   };

//   return (
//     <div>
//       <button onClick={handleLogout}>로그아웃</button>
//     </div>
//   );
// };

// export default Logout;
