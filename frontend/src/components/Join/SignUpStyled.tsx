import styled from "styled-components";

export const StyledContainer = styled.div`
  color: #333333;
  padding: 20px;
  max-width: 400px;
  margin: auto;
`;

export const StyledFormItem = styled.div`
  margin: 5px 0 5px 0;
  display: flex;
  width: 350px;

  label {
    margin-right: 60px;
  }

  input,
  button {
    flex: 2;
    padding: 5px;
    outline: none;
  }

  input {
    border: none;
    border-bottom: 1px solid #333333;
    background: white;
  }

  button {
    flex: 1;
    padding: 8px;
    background-color: #a3cca3;
    color: #333333;
    cursor: pointer;
    textAlign: "center",
    &:hover {
      background-color: #cccccc;
    }
  }
  span {
    margin-left: 30px;
  }

  input[type="radio"] {
    background-color: white;
  }
`;

export const GreenText = styled.span`
  color: green;
`;

export const RedText = styled.span`
  color: red;
`;

export const StyledSignUp = styled.p`
  color: red;
  text-align: center;
  margin-top: 10px;
`;

export const StyledText = styled.p`
  text-align: center;
  margin-top: 10px;
`;
