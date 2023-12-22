import styled from "styled-components";
export const UserProfileContainer = styled.div`
  padding: 20px;
  border-radius: 8px;
  margin: auto;
  width: 70%;
`;

export const UserProfileTitle = styled.h2`
  font-size: 1.5rem;
  margin-bottom: 10px;
  text-align: center;
`;

export const UserProfileInfo = styled.div`
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
  }

  select {
    padding: 5px;
    outline: none;
    border: none;
    border-bottom: 1px solid #333333;
    border-radius: 0;
    background: white;
  }
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
