import naverImg from "../../assets/naverImg.jpg";
import styled from "styled-components";

const Naver = () => {
  const naverAuthUrl = `https://localhost:5173/oauth2/authorization/naver`;

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
