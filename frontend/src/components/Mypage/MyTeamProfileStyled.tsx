import styled from "styled-components";

export const TeamProfileContainer = styled.div`
  display: flex;
  flex-direction: row;
  padding: 10px 20px 20px 20px;
  border-radius: 8px;
  margin: auto;
  width: 35%;
`;

export const TeamProfileTitle = styled.div`
  margin-bottom: 30px;
  font-weight: bold;
  font-size: 1.2rem;
  text-align: left;
  color: #333;
`;

export const ContainerWrapper = styled.div`
  display: flex;
  justify-content: space-between;
  width: 100%;
  margin-bottom: 20px;
`;

export const ImageUploadContainer = styled.span`
  display: flex;
  align-items: center;
  margin-bottom: 15px;

  label {
    cursor: pointer;
    padding: 8px;
    border-radius: 4px;
    margin-bottom: 10px;
    margin-right: 10px;
  }

  input {
    display: none;
  }

  img {
    max-width: 80px;
    max-height: 80px;
  }
`;

export const NicknameContainer = styled.span`
  display: flex;
  align-items: center;
  margin-left: 20px;
  input {
    flex: 1;
    margin-right: 10px;
    margin-bottom: 10px;
    padding: 5px;
    outline: none;
    border: none;
    border-bottom: 1px solid #333333;
    border-radius: 0;
  }
`;

export const ButtonContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
`;

export const UpdateButton = styled.button`
  background-color: #cccccc;
  color: #333333;
  cursor: pointer;
  margin-right: 40px;
  border: none;
  border-radius: 20px;

  &:hover {
    background-color: #a3cca3;
  }
`;

export const DeleteButton = styled.button`
  background-color: #a3cca3;
  color: #333333;
  cursor: pointer;
  border: none;
  border-radius: 20px;

  &:hover {
    background-color: #cccccc;
  }
`;
