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
      <>
      <button onClick={loginHandler}>네이버 로그인</button>
      </>
    </div>
  )
}

export default Naver;