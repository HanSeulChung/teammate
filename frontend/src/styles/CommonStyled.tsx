import styled from "styled-components";
import { Link } from "react-router-dom";

export const CommonSubmitBtn = styled.button`
  background-color: #a3cca3;
`;

export const StyledLink = styled(Link)`
  text-decoration: none;
  &:hover {
    color: #a3cca3;
  }
`;
