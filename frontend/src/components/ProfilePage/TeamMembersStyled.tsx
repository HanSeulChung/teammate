import styled from "styled-components";

export const TeamProfileContainer = styled.div`
  text-align: center;
  position: absolute;
  top: 45%;
  left: 50%;
  transform: translate(-50%, -50%);
`;
export const TeamProfileTitle = styled.h2`
  text-align: center;
  margin-bottom: 30px;
  font-weight: bold;
  font-size: 1.5em;
`;

export const TeamInfoContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
`;

export const TeamImage = styled.img`
  max-width: 150px;
  max-height: 150px;
  border-radius: 20px;
`;

export const SelectContainer = styled.div`
  text-align: center;
  margin-top: 10px;
`;

export const SearchInput = styled.input`
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 5px;
  box-sizing: border-box;
  margin: 20px 0 10px 0;
  background: white;
  width: 100%;
`;

export const MemberContainer = styled.div`
  max-height: 150px;
  overflow-y: auto;
  border: 1px solid #ccc;
  border-radius: 5px;
  padding: 5px;
  display: flex;
  flex-direction: column;
`;

export const MemberInput = styled.input`
  width: 100%;
  box-sizing: border-box;
  margin-bottom: 5px;
  border: none;
  padding: 5px;
  background-color: transparent;
  cursor: pointer;
  &:hover {
    background-color: #f0f0f0;
  }
`;

export const MoveTeamPage = styled.div`
  text-align: right;
  margin-top: 20px;
  position: absolute;
  top: 100px;
  right: 250px;
  margin: 20px;
  cursor: pointer;
  &:hover {
    color: #a3cca3;
  }
`;
