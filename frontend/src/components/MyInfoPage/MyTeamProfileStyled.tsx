import styled from "styled-components";

export const TeamProfileContainer = styled.div`
  position: absolute;
  top: 100px;
  left: 27%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 10px 20px 20px 20px;
  border-radius: 8px;
  margin: auto;
  width: 45%;
`;

export const TeamProfileTitle = styled.div`
  margin-bottom: 30px;
  font-weight: bold;
  font-size: 1.2rem;
  text-align: center;
  color: #333;
`;

export const ContainerWrapper = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 20px;
`;

export const ImageUploadContainer = styled.span`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 30px;

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
    max-width: 150px;
    max-height: 150px;
    border-radius: 15px;
  }
`;

export const NicknameContainer = styled.span`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 20px;
  input {
    flex: 1;
    margin-bottom: 10px;
    padding: 5px;
    outline: none;
    border: none;
    border-bottom: 1px solid #333333;
    border-radius: 0;
    text-align: center;
    background: white;
  }
`;

export const ButtonContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
`;

export const UserProfileInfo = styled.div`
  width: 100%;
  margin-bottom: 30px;
  p {
    margin: 0;
    margin-bottom: 15px;
    text-align: left;
  }
  span {
    margin-bottom: 15px;
    display: flex;
    justify-content: space-between;
  }

  label {
    margin-right: 10px;
    font-weight: bold;
    margin: auto;
  }

  select {
    width: 30%;
    padding: 10px;
    outline: none;
    border: none;
    border-bottom: 1px solid #333333;
    border-radius: 0;
    background: white;
    margin-left: 35%;
  }
`;

export const UserProfileTitle = styled.h2`
  font-size: 1.5rem;
  text-align: center;
`;

export const TeamProfileBox = styled.div`
  width: 100%;
  text-align: center;
`;

export const Button = styled.button`
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

export const LinkContainer = styled.div`
  text-align: right;
  margin-right: 0;
`;

export const TeamDisband = styled.div`
  margin-top: 20px;
  color: red;
  cursor: pointer;
`;
