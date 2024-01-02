import { Link } from "react-router-dom";
import styled from "styled-components";
import mainImage from "../assets/main_image.png";

const MainContents = () => {
  return (
    <MainContentsSpan>
      <div>
        <h2 className="mb-4 text-4xl font-extrabold leading-none tracking-tight text-gray-900 md:text-5xl lg:text-6xl">
          팀원들과의 원활한 협업을 위한{" "}
          <span className="text-mainGreen">TeamMate,</span> 지금 시작해보세요.
        </h2>
        <p className="mb-4 text-lg font-normal text-gray-500 lg:text-xl">
          글을 작성하고 계획을 세우며, 다른 이들과 협업해보세요.
        </p>
        <Link
          to="/signUp"
          className="inline-flex items-center justify-center px-5 py-3 text-base font-medium text-center text-white bg-mainGreen rounded-lg hover:bg-green-800 focus:ring-4 focus:ring-green-300"
        >
          시작하기
        </Link>
      </div>
      <img src={mainImage} />
    </MainContentsSpan>
  );
};

export default MainContents;

// 스타일드 컴포넌트
export const MainContentsSpan = styled.span`
  margin-top: 200px;
  display: flex;
  align-items: center;
  justify-content: space-between;

  & > div {
    max-width: 60%;
  }

  & > div > h2 {
    font-size: 3rem;
  }

  & > div > p {
    font-size: 1.5rem;
  }

  & > img {
    max-with: 40%;
  }
`;
