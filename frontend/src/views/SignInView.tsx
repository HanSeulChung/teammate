import SignIn from "../components/Login/SignIn";
import Kakao from "../components/Login/Kakao";
import Naver from "../components/Login/Naver";
import Google from "../components/Login/Google";
import styled from "styled-components";

const SignInView = () => {
  const StyledSignInView = styled.section`
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  `;

  return (
    <>
      <StyledSignInView>
        <SignIn />
        <Kakao />
        <Naver />
        <Google />
      </StyledSignInView>
    </>
  );
};

export default SignInView;