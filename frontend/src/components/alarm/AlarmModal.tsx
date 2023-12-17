// AlarmModal.tsx
import React, { useState } from "react";
import styled from "styled-components";
import PersonalAlarm from "./PersonalAlarm";
import TeamAlarm from "./TeamAlarm";

const ModalOverlay = styled.div`
  position: fixed;
  top: 0px;
  right: 50px;
  margin: 50px;
  width: 20%;
  height: 60%;

  z-index: 1000;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  padding: 20px;
`;

const ModalContent = styled.div`
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 100%;
  max-height: 80vh; /* 최대 높이를 조정합니다. */
  overflow-y: auto; /* 스크롤을 추가합니다. */
  position: relative; /* 부모 기준으로 자식을 위치시킵니다. */
`;

const TabButtons = styled.div`
  display: flex;
  margin-bottom: 10px;
`;

const TabButton = styled.button<{ active: boolean }>`
  margin-right: 10px;
  padding: 5px 10px;
  cursor: pointer;
  border: none;
  background-color: transparent;
  font-weight: bold;
  color: ${(props) => (props.active ? "green" : "black")};

  &:hover {
    text-decoration: underline;
  }
`;

const CloseButton = styled.button`
  position: absolute;
  top: 15px;
  right: 15px;
  cursor: pointer;
`;

interface AlarmModalProps {
  closeModal: () => void;
}

const AlarmModal: React.FC<AlarmModalProps> = ({ closeModal }) => {
  const [activeTab, setActiveTab] = useState<"personal" | "team">("personal");

  const switchTab = (tab: "personal" | "team") => {
    setActiveTab(tab);
  };

  const handleModalClick = (e: React.MouseEvent) => {
    e.stopPropagation(); // 모달 내부를 클릭해도 닫히지 않도록 합니다.
  };

  return (
    <ModalOverlay onClick={closeModal}>
      <ModalContent onClick={handleModalClick}>
        <CloseButton onClick={closeModal}>닫기</CloseButton>
        <TabButtons>
          <TabButton
            onClick={() => switchTab("personal")}
            active={activeTab === "personal"}
          >
            개인 알람
          </TabButton>
          <TabButton
            onClick={() => switchTab("team")}
            active={activeTab === "team"}
          >
            팀 알람
          </TabButton>
        </TabButtons>
        {activeTab === "personal" ? <PersonalAlarm /> : <TeamAlarm />}
      </ModalContent>
    </ModalOverlay>
  );
};

export default AlarmModal;
