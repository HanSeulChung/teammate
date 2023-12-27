import { Link } from "react-router-dom";
import styled from "styled-components";
import { useState } from "react";
import axiosInstance from "../../axios";

export default function HomeCreateTeamBtn() {
  const [inviteCode, setInviteCode] = useState("");

  const handleInputInviteCode = (event: any) => {
    setInviteCode(event.target.value);
  };

  const handleCreateTeam = () => {
    if (!inviteCode) {
      alert("초대 코드를 입력하세요.");
      return;
    }
    axiosInstance
      .get(`/team/${inviteCode}`)
      .then((response) => {
        console.log(response.data);
        alert("팀에 참가되었습니다.");
        window.location.reload();
      })
      .catch((error) => {
        console.error("API call error:", error);
        if (error.response) {
          switch (error.response.status) {
            case 401:
              alert("토큰 값이 유효하지 않습니다.");
              break;
            case 404:
              alert("팀이 해체되었습니다.");
              break;
            case 409:
              alert("팀에 이미 참가한 경우입니다.");
              break;
            case 422:
              alert("회원이 존재하지 않습니다.");
              break;
            case 429:
              alert("팀 인원 제한에 걸렸습니다.");
              break;
            default:
              alert("API 호출 중 오류가 발생했습니다.");
              break;
          }
        } else {
          alert("이미 참가된 팀입니다.");
        }
      });
  };

  return (
    <CenteredContainer>
      <Link to="/teamInfo">
        <CreateTeamButton>+ 팀 생성하기</CreateTeamButton>
      </Link>
      <TeamNameInput
        type="text"
        placeholder="초대코드를 입력하세요"
        value={inviteCode}
        onChange={handleInputInviteCode}
      />
      <CreateTeamButton onClick={handleCreateTeam}>참가</CreateTeamButton>
    </CenteredContainer>
  );
}

const CenteredContainer = styled.div`
  position: absolute;
  width: 1000px;
  top: 210px;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const CreateTeamButton = styled.button`
  margin-top: 20px;
  padding: 8px 16px;
  background-color: #a3cca3;
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  color: #333333;

  &:hover {
    background-color: #cccccc;
  }
`;

const TeamNameInput = styled.input`
  margin-top: 20px;
  margin-right: 5px;
  margin-left: 30px;
  padding: 8px;
  border: 1px solid #cccccc;
  border-radius: 20px;
  outline: none;
`;
