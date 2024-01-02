import SignIn from "../components/Login/SignIn";
import Naver from "../components/Login/Naver";
import styled from "styled-components";

const SignInView = () => {
  return (
    <>
      <StyledSignInView>
        <SignIn />
        {/* <Kakao /> */}
        <Naver />
        {/* <Google /> */}
      </StyledSignInView>
    </>
  );
};

export default SignInView;

const StyledSignInView = styled.section`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding-top: 200px;
`;
