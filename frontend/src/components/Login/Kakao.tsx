import React from "react";

const Kakao = () => {
  const REST_API_KEY = import.meta.env.VITE_REST_API;
  const REDIRECT_URI = "http://118.67.128.124:8080/kakaoLogin";
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
