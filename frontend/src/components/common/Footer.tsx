import styled from "styled-components";

const Footer = () => {
  return (
    <FooterTag>
      <FooterDiv>
        <FooterParagraph>
          백프로 팀의 협업 프로젝트 TeamMate<br />
          2023.11.16 ~ 2024.01.04
        </FooterParagraph>
        <a href="https://github.com/100backfro/teammate">
          <svg className="w-4 h-4 inline align-middle" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 20 20">
            <path fillRule="evenodd" d="M10 .333A9.911 9.911 0 0 0 6.866 19.65c.5.092.678-.215.678-.477 0-.237-.01-1.017-.014-1.845-2.757.6-3.338-1.169-3.338-1.169a2.627 2.627 0 0 0-1.1-1.451c-.9-.615.07-.6.07-.6a2.084 2.084 0 0 1 1.518 1.021 2.11 2.11 0 0 0 2.884.823c.044-.503.268-.973.63-1.325-2.2-.25-4.516-1.1-4.516-4.9A3.832 3.832 0 0 1 4.7 7.068a3.56 3.56 0 0 1 .095-2.623s.832-.266 2.726 1.016a9.409 9.409 0 0 1 4.962 0c1.89-1.282 2.717-1.016 2.717-1.016.366.83.402 1.768.1 2.623a3.827 3.827 0 0 1 1.02 2.659c0 3.807-2.319 4.644-4.525 4.889a2.366 2.366 0 0 1 .673 1.834c0 1.326-.012 2.394-.012 2.72 0 .263.18.572.681.475A9.911 9.911 0 0 0 10 .333Z" clipRule="evenodd"/>
          </svg>
          GitHub
        </a>
      </FooterDiv>
    </FooterTag>
  );
};

export default Footer;

// 스타일드 컴포넌트 

export const FooterTag = styled.footer`
  background-color: #F5F6F7;
  color: #999999;
  width:100vw;
  margin-left: calc(-50vw + 50%);   
  height: 10rem;

  & > p {
    margin: 0 auto;
    max-width: 1280px;
    min-width: 1024px; 
    padding-top: 2.9rem;
  }
`

export const FooterDiv = styled.div`
  width: 100%;
  max-width: 1024px;
  height: 100%;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  // padding-top: 60px;
`

const FooterParagraph = styled.p`
  // padding-top: 60px;
`