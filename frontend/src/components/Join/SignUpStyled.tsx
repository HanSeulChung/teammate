import styled from 'styled-components';

export const StyledContainer = styled.div`
  color: #333333;
  padding: 20px;
  max-width: 300px;
  margin: auto;
`;

export const StyledFormItem = styled.div`
  margin: 5px 0 5px 0;
  display: flex;

  label {
    margin-right: 60px; 
  }

  input, button {
    flex: 2;
    padding: 5px;
    outline: none; 
  }

  input {
    border: none;
    border-bottom: 1px solid #333333; 
  }

  button {
    flex: 1;
    margin-left: 10px;
    padding: 8px;
    background-color: #A3CCA3;
    color: #333333;
    cursor: pointer;

    &:hover {
      background-color: #cccccc;
    }
  }
`;
