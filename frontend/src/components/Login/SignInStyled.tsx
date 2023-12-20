import styled from "styled-components";

export const StyledContainer = styled.div`
  color: #333333;
  padding: 20px;
  width: 25%;
  margin: auto;
`;

export const StyledFormItem = styled.div`
  margin: 5px 0 5px 0;
  display: flex;

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
    margin-left: 10px;
    padding: 8px;
    background-color: #a3cca3;
    color: #333333;
    cursor: pointer;

    &:hover {
      background-color: #cccccc;
    }
  }
`;
