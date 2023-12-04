import styled from 'styled-components';
import {Link} from "react-router-dom";

// 헤더 태그
export const HeaderTag = styled.header`
    display: flex;
    justify-content: space-between;
    margin-bottom: 2rem;
`

// 헤더 로고 이미지
export const HeaderImg = styled.img`
    width: 70px;
    height: 70px;
`

// 헤더 버튼 리스트
export const HeaderUl = styled.ul`
    list-style: none;
    padding: 0;
    display: flex;
    margin-top: 25px;

    & > li:first-child {
        margin-right: 16px;
    }
`

// 헤더 시작하기 버튼
export const HeaderLink = styled(Link)`
    border: 3px solid #CCCCCC;
    padding: 2px 10px;
    border-radius: 5px;
`