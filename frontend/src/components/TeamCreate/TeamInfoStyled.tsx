// TeamInfoStyled.js (스타일 파일)

import styled from "styled-components";

export const StyledContainer = styled.div`
  color: #333333;
  padding: 20px;
  width: 30%;
  margin: auto;
`;

export const StyledFormItem = styled.div`
  margin: 10px 0;
  display: flex;
  align-items: center;

  label {
    margin-right: 20px;
    font-weight: bold;
  }

  input,
  select,
  button {
    width: 100%;
    flex: 1;
    padding: 8px;
    outline: none;
    border: none;
    border-bottom: 1px solid #333333;
  }

  input,
  select {
    border-radius: 0;
    flex-direction: column; /* 수직으로 배치 */
    align-items: flex-start;
    background: white;
  }

  input {
    margin: 3px;
  }

  button {
    margin-top: 20px;
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
  margin-top: 10px;
`;

export const StyledHeading = styled.h2`
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 30px;
  text-align: center;
`;

export const StyledLabel = styled.label`
  margin-right: 20px;
  font-weight: bold;
  display: none;
`;

export const StyledInput = styled.input`
  flex: 1;
  padding: 8px;
  outline: none;
  border: none;
  border-bottom: 1px solid #333333;
  border-radius: 0;
  margin-top: 10px;
`;

export const StyledSelect = styled.select`
  flex: 1;
  padding: 8px;
  outline: none;
  border: none;
  border-bottom: 1px solid #333333;
  border-radius: 0;
`;

export const StyledButton = styled.button`
  flex: 1;
  margin-left: 10px;
  background-color: #a3cca3;
  color: #333333;
  cursor: pointer;
  border-bottom: none;
  &:hover {
    background-color: #cccccc;
  }
`;

export const ImageUploadContainer = styled.span`
  display: flex;
  align-items: center;
  margin: 20px 20px 0 30px;

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
