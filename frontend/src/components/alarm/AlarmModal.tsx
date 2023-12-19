// AlarmModal.tsx
import React, { useState } from "react";
import styled from "styled-components";
import PersonalAlarm from "./PersonalAlarm";
import TeamAlarm from "./TeamAlarm";

interface AlarmModalProps {
  closeModal: () => void;
}
// 가짜 데이터
const fakePersonalAlarmProps = {
  content: "알람 내용",
  date: "2023-12-17", // 적절한 날짜로 대체
  onDelete: () => {
    // 삭제 로직 구현
  },
};

const AlarmModal: React.FC<AlarmModalProps> = ({ closeModal }) => {
  const [activeTab, setActiveTab] = useState<"personal" | "team">("personal");

  const switchTab = (tab: "personal" | "team") => {
    setActiveTab(tab);
  };

  const handleModalClick = (e: React.MouseEvent) => {
    e.stopPropagation(); // 모달 내부를 클릭해도 닫히지 않도록
  };

  // const StyledTabButton = TabButton.withComponent("div");

  return (
    <ModalOverlay onClick={closeModal}>
      <ModalContent onClick={handleModalClick}>
        <CloseButton onClick={closeModal}>닫기</CloseButton>
        <TabButtons>
          <TabButton
            onClick={() => switchTab("personal")}
            active={activeTab === "personal" ? "true" : "false"}
          >
            개인 알람
          </TabButton>
          <TabButton
            onClick={() => switchTab("team")}
            active={activeTab === "team" ? "true" : "false"}
          >
            팀 알람
          </TabButton>
        </TabButtons>
        {activeTab === "personal" ? (
          <PersonalAlarm {...fakePersonalAlarmProps} />
        ) : (
          <TeamAlarm {...fakePersonalAlarmProps} />
        )}
      </ModalContent>
    </ModalOverlay>
  );
};

export default AlarmModal;

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
  max-height: 80vh;
  overflow-y: auto;
  position: relative;
`;

const TabButtons = styled.div`
  display: flex;
  margin-bottom: 10px;
`;

const TabButton = styled.button<{ active: string }>`
  margin-right: 10px;
  padding: 5px 10px;
  cursor: pointer;
  border: none;
  background-color: transparent;
  font-weight: bold;
  color: ${(props: { active: string }) =>
    props.active === "true" ? "green" : "black"};

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
