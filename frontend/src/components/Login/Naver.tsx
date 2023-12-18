import React from 'react'


const Naver = () => {
  const clientID = 'yd0R9uz74I8IUUb7kOQ2';
  const callbackUrl = 'http://localhost:3000/naverLogin';
  const stateString = 'Ydqk2Xb9Dj';
  const naverAuthUrl = `https://nid.naver.com/oauth2.0/authorize?client_id=${clientID}&response_type=code&redirect_uri=${callbackUrl}&state=${stateString}`;

  const loginHandler = () => {
    window.location.href = naverAuthUrl;
  };
  return (
    <div>
        <button className="btn btn-wide btn-ghost border-slate-300 no-animation" onClick={loginHandler}>
            <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" /></svg>
            네이버 계정으로 로그인
        </button>
    </div>
  )
}

export default Naver;