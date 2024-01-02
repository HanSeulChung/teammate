import styled from "styled-components";

export const TeamLeaderContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  height: 100vh;
  margin-top: 10px;
`;

export const CenteredContainer = styled.div`
  margin-top: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  .modal-content {
    padding: 20px;
    background: #cccccc;
  }
  input,
  select {
    width: 100px;
    background-color: #cccccc;
    border: 1px solid white;
    padding: 6px;
    margin-bottom: 8px;
  }
`;

export const TeamLeaderModal = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
`;

export const StyledButton = styled.button`
  margin-top: 10px;
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

export const TeamProfileSection = styled.div`
  display: flex;
  justify-content: space-between;
`;

export const TeamImageContainer = styled.div`
  margin: 0;
`;

export const TeamInfoContainer = styled.div`
  input,
  select {
    width: 100px;
    background-color: white;
    border: 1px solid #cccccc;
    border-radius: 10px;
  }
`;

export const StyledInput = styled.input`
  width: 75%;
  background-color: white;
  border: 1px solid #cccccc;
  border-radius: 10px;
  padding: 6px;
  margin-right: 5px;
`;

export const TeamMembersContainer = styled.div`
  width: 350px;
  border: 1px solid #cccccc;
  border-radius: 5px;
  padding: 20px;
  margin-top: 20px;
`;

export const ConfirmationModal = styled.div`
  position: absolute;
  top: 35%;
  left: 50%;
  transform: translate(-50%, -50%);
  padding: 20px;
  background: white;
  z-index: 999;
  border: 1px solid #cccccc;
  width: 250px;
  height: 170px;

  input {
    background: white;
    border: 1px solid #cccccc;
    margin-right: 10px;
  }
`;

export const MoveTeamPage = styled.div`
  text-align: right;
  margin-right: 100px;
  cursor: pointer;
  &:hover {
    color: #a3cca3;
  }
`;

export const TeamName = styled.h3`
  text-align: center;
  font-size: 1.5rem;
  font-weight: bold;
`;

export const Img = styled.img`
  width: 150px;
  height: 150px;
  cursor: pointer;
  margin: 20px 0 20px 30px;
  border-radius: 20px;
`;

export const InputOne = styled.input`
display:none;
width:100%:`;

export const InputTwo = styled.input`
  width: 150px;
  height: 40px;
  margin-right: 10px;
  padding: 5px;
`;

export const SelectLeader = styled.select`
  width: 150px;
  height: 40px;
  margin-right: 10px;
`;

export const InviteCode = styled.span`
  display: flex;
  align-items: center;
`;

export const ImgTwo = styled.img`
  width: 30px;
  cursor: pointer;
`;

export const SearchMember = styled.div`
  margin-top: 10px;
  max-height: 250px;
  overflow-y: auto;
`;

export const MemberList = styled.div`
  display: flex;
  margin-bottom: 10px;
`;

export const DeleteTeam = styled.div`
  text-align: center;
  margin-top: 20px;
`;

export const RedText = styled.p`
  color: red;
`;
