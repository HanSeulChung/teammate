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
      <TeamInvite>
        <input
          type="text"
          placeholder="초대코드를 입력하세요"
          value={inviteCode}
          onChange={handleInputInviteCode}
        />
      </TeamInvite>
      <CreateTeamButton onClick={handleCreateTeam}>참가</CreateTeamButton>
    </CenteredContainer>
  );
}

const CenteredContainer = styled.div`
  margin: 20px 0 10px 0;
  display: flex;
  align-items: center;
  justify-content: center;
  @media (max-width: 600px) {
    flex-direction: column;
  }
`;

const CreateTeamButton = styled.button`
  padding: 8px 16px;
  background-color: #a3cca3;
  color: white;
  border: none;
  border-radius: 10px;
  cursor: pointer;

  &:hover {
    background-color: #cccccc;
  }
  @media (max-width: 600px) {
    margin-top: 10px;
  }
`;

const TeamInvite = styled.span`
  position: relative;
  padding: 5px;
  margin-right: 5px;
  margin-left: 30px;
  @media (max-width: 600px) {
    margin-top: 10px;
  }

  input {
    outline: none;
    border: none;
    width: 150px;
  }

  &:before {
    content: "";
    position: absolute;
    left: 0;
    right: 0;
    bottom: 0;
    height: 1px;
    background-color: #cccccc;
  }
`;
