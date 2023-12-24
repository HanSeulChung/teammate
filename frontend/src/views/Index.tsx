import MainContents from "../components/MainContents";
import { HeaderLink } from "../styles/HeaderStyled";

const Index = () => {
  return (
    <main>
      <MainContents />
      <HeaderLink to="/signUp" className="btn btn-outline btn-accent">시작하기</HeaderLink>
    </main>
  );
};

export default Index;