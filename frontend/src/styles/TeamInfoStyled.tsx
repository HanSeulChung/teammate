import styled from "styled-components";

export const StyledContainer = styled.div`
  color: #333333;
  padding: 20px;
  width: 25%;
  margin: auto;
`;

export const StyledFormItem = styled.div`
  margin: 5px 0;

  label {
    margin-right: 20px;
    font-weight: bold;
  }

  input,
  select,
  button {
    flex: 2;
    padding: 8px;
    outline: none;
  }

  input,
  select {
    border: 1px solid #333333;
    border-radius: 4px;
  }

  button {
    flex: 1;
    margin-left: 10px;
    background-color: #a3cca3;
    color: #333333;
    cursor: pointer;

    &:hover {
      background-color: #cccccc;
    }
  }
`;

export const StyledImagePreview = styled.div`
  img {
    max-width: 100px;
    max-height: 100px;
    margin-top: 10px;
  }
`;

export const StyledErrorMessage = styled.div`
  color: red;
  margin-top: 5px;
`;

export const StyledHeading = styled.h2`
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 10px;
`;