import styled from "styled-components";

export const MainContentsSpan = styled.span`
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
`