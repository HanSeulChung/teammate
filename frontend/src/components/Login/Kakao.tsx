import React from "react";

const Kakao = () => {
  const REST_API_KEY = "85e05825f4b6ddd80101b7a8eb2486b0";
  const REDIRECT_URI = "http://localhost:3000/kakaoLogin";
  const kakaoAuthUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}&response_type=code`;

  const loginHandler = () => {
    window.location.href = kakaoAuthUrl;
  };
  return (
    <div>
      <>
        <button onClick={loginHandler}>카카오톡 로그인</button>
      </>
    </div>
  );
};

export default Kakao;
