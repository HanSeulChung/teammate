import React from "react";

const Kakao = () => {
  const REST_API_KEY = import.meta.env.VITE_FAKE_STORE_API;
  const REDIRECT_URI = "http://localhost:3000/kakaoLogin";
  const kakaoAuthUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}&response_type=code`;

  const loginHandler = () => {
    window.location.href = kakaoAuthUrl;
  };
  return (
    <div>
        <button className="btn btn-wide btn-ghost border-slate-300 no-animation" onClick={loginHandler}>
            <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" /></svg>
            카카오톡 계정으로 로그인
        </button>
    </div>
  );
};

export default Kakao;