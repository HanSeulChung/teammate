import styled from 'styled-components';

export const FooterTag = styled.footer`
    background-color: #F5F6F7;
    color: #999999;
    width:100vw;
    margin-left: calc(-50vw + 50%);   
    height:150px;

    & > p {
        margin: 0 auto;
        max-width: 1280px;
        min-width: 1024px; 
        padding-top: 2.9rem;
    }
`