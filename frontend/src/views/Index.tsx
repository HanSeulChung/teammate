import MainContents from "../components/MainContents";
import { HeaderLink } from "../styles/HeaderStyled";

const Index = () => {
  return (
    <main>
      <MainContents />
      <HeaderLink to="/signup">시작하기</HeaderLink>
    </main>
  );
};

export default Index;
