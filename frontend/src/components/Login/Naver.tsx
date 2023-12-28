import naverImg from "../../assets/naverImg.jpg";
import styled from "styled-components";

const Naver = () => {
  const clientID = import.meta.env.VITE_REST_API;
  const callbackUrl = import.meta.env.VITE_REDIRECT_KEY;
  const stateString = import.meta.env.VITE_STATE;
  const naverAuthUrl = `https://nid.naver.com/oauth2.0/authorize?client_id=${clientID}&response_type=code&redirect_uri=${callbackUrl}&state=${stateString}`;

  const loginHandler = () => {
    window.location.href = naverAuthUrl;
  };
  return (
    <div>
      <button
        className="btn btn-wide btn-ghost border-slate-300 no-animation"
        onClick={loginHandler}
      >
        <Img src={naverImg} alt="naver" />
        네이버 계정으로 로그인
      </button>
    </div>
  );
};

export default Naver;

const Img = styled.img`
  width: 25px;
  height: 25px;
`;
