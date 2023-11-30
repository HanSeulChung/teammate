import styled from "styled-components";

// 스타일드 컴포넌트
export const Modal = styled.div`
    width: 100vw;
    height: 100vh;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    position: fixed;
    z-index: 99999999;
`

export const Overlay = styled.div`
    background: rgba(49,49,49,0.8);
    width: 100vw;
    height: 100vh;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    position: fixed;
`

export const ModalContent = styled.div`
    position: absolute;
    top: 40%;
    left: 50%;
    transform: translate(-50%, -50%);
    line-height: 1.4;
    background: #f1f1f1;
    padding: 14px 28px;
    border-radius: 3px;
    max-width: 600px;
    min-width: 300px;
    z-index: 6;
`

export const CloseModal = styled.button`
    position: absolute;
    top: 10px;
    right: 10px;
    padding: 5px 7px;
`