import styled from "styled-components";

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

export const TeamLeaderContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 70vh;
`;

export const TeamProfileSection = styled.div`
  display: flex;
  justify-content: space-between;
`;

export const TeamImageContainer = styled.div`
  /* Add styles as needed */
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
  top: 65%;
  left: 50%;
  transform: translate(-50%, -50%);
  padding: 20px;
  background: white;
  z-index: 999;
  border: 1px solid #cccccc;
  width: 30%;
  height: 17%;

  input {
    background: white;
    border: 1px solid #cccccc;
    margin-right: 10px;
  }
`;
