import React, { useState, useEffect } from "react";
import styled from "styled-components";
import { useRecoilValue } from "recoil";
import { accessTokenState } from "../../state/authState";
import { fetchPersonalAlarms } from "./AlarmApiService";

interface TeamAlarmProps {
  content: string;
  date: string;
  onDelete: () => void;
}

const TeamAlarm: React.FC<TeamAlarmProps> = ({ content, date, onDelete }) => {
  const [alarms, setAlarms] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [error] = useState<string | null>(null);
  const accessToken = useRecoilValue(accessTokenState);

  useEffect(() => {
    fetchPersonalAlarms(accessToken).then((data) => {
      setAlarms(data);
      setLoading(false);
    });
  }, []);

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  return (
    <div>
      {alarms.map((alarm) => (
        <AlarmContainer key={alarm.typeId}>
          <AlarmContent>{content} (개인 알람 내용)</AlarmContent>
          <DateInfo>{date}</DateInfo>
          <DeleteButton onClick={onDelete}>삭제</DeleteButton>
        </AlarmContainer>
      ))}
    </div>
  );
};

export default TeamAlarm;

const AlarmContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  border: 1px solid #ccc;
  padding: 10px;
  margin-bottom: 10px;
`;

const AlarmContent = styled.p`
  flex-grow: 1;
  margin-right: 10px;
  color: black;
`;

const DateInfo = styled.p`
  color: #888;
  font-size: 12px;
  margin-right: 10px;
`;

const DeleteButton = styled.button`
  color: red;
  font-weight: bold;
  cursor: pointer;
  background: #a3cca3;
`;
