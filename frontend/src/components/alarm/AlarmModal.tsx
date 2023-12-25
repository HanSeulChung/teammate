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
  date: "2023-12-17",
  onDelete: () => {
    console.log("개인 알람이 삭제되었습니다.");
  },
};

const AlarmModal: React.FC<AlarmModalProps> = ({ closeModal }) => {
  const [activeTab, setActiveTab] = useState<"personal" | "team">("personal");
  const [alarmList, setAlarmList] = useState([fakePersonalAlarmProps]);

  const switchTab = (tab: "personal" | "team") => {
    setActiveTab(tab);
  };

  const handleModalClick = (e: React.MouseEvent) => {
    e.stopPropagation();
  };

  const handleDeletePersonalAlarm = () => {
    setAlarmList((prevList) => {
      const updatedList = prevList.filter(
        (alarm) => alarm !== fakePersonalAlarmProps,
      );
      console.log("개인 알람이 삭제되었습니다.");
      return updatedList;
    });
  };

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
          <PersonalAlarm
            {...fakePersonalAlarmProps}
            onDelete={handleDeletePersonalAlarm}
          />
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
  top: 8%;
  right: 15%;
  width: 25%;
  display: flex;
  justify-content: center;
  align-items: center;
`;

const ModalContent = styled.div`
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 80%;
  max-width: 600px;
  max-height: 80vh;
  overflow-y: auto;
  position: relative;
  border: 1px solid #cccccc;
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
  right: 30px;
  cursor: pointer;
  background: #cccccc;
`;
