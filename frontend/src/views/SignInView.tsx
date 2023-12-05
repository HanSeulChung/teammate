import SignIn from "../components/SignIn";
import Kakao from "../components/Kakao";
// import Naver from "../components/Naver";
// import Google from "../components/Google";
import styled from "styled-components";

const SignInView = () => {
  const StyledSignInView = styled.section`
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  `

  return(
    <>
    <StyledSignInView>
      <SignIn />
      <Kakao />
      {/* <Naver />
      <Google /> */}
    </StyledSignInView>
    </>
  )
}

export default SignInView;