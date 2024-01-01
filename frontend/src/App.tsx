import "./App.css";
import { BrowserRouter } from "react-router-dom";
import Router from "./router/router";
import Header from "./components/common/Header";
import Footer from "./components/common/Footer";
import styled from "styled-components";

function App() {
  return (
    <BrowserRouter>
      <FullWidthDiv>
        <Header />
        <WrapperDiv>
          <section>
            <Router />
          </section>
        </WrapperDiv>
        <Footer />
      </FullWidthDiv>
    </BrowserRouter>
  );
}

export default App;

export const FullWidthDiv = styled.div`
  position: relative;
  width: 100vw;
  margin-left: -50vw;
  height: 100%;
  left: 50%;
`;

export const WrapperDiv = styled.div`
  // padding: 0 10% 0;
  margin: 0 auto;
  min-height: 1080px;

  @media (min-width: 1024px) {
    max-width: 1024px;
    min-height: 1080px;
    // width: 1024px;
    height: auto;
  }
  @media (max-height: 900px) {
    min-height: 600px;
  }
  @media (max-width: 480px) {
    max-width: 480px;
    padding: 0 5% 0;
    min-height: 320px;
  }
`;
