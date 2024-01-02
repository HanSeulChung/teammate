import styled from "styled-components";

const Footer = () => {
  return (
    <FooterTag>
      <FooterDiv>
        <p>
          Lorem ipsum dolor sit amet consectetur adipisicing elit.<br />
          Numquam animi molestiae doloremque quam saepe officiis laboriosam,
          nostrum porro similique tempore.
        </p>
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
`