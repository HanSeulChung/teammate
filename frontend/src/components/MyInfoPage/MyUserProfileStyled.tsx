import styled from "styled-components";
export const UserProfileContainer = styled.div`
  padding: 20px;
  border-radius: 8px;
  margin: auto;
  width: 600px;
  padding-top: 50px;
`;

export const UserProfileTitle = styled.h2`
  font-size: 1.5rem;
  margin-bottom: 10px;
  text-align: center;
`;
export const Email = styled.span`
  flex: 1.3;
  margin: 10px 0;
`;

export const UpdateButton = styled.button`
  flex: 0.7;
  margin-left: 20px;
  padding: 0;
  background-color: #cccccc;
  color: #333333;
  cursor: pointer;
  border: none;
  border-radius: 20px;

  &:hover {
    background-color: #a3cca3;
  }
`;
export const ErrorContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: flex-end;
`;
export const ErrorText = styled.p`
  color: red;
  margin-top: 10px;
`;
export const Button = styled.button`
  margin-top: 10px;
  padding: 8px 16px;
  background-color: #cccccc;
  color: white;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  color: #333333;
  &:hover {
    background-color: #a3cca3;
  }
`;

export const ButtonContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: flex-end;
`;
export const LinkContainer = styled.div`
  text-align: right;
`;
