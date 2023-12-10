import React, { useState } from "react";
import { useSearchState } from "../../state/authState";
import styled from "styled-components";
import searchImg from "../../assets/search.png";

const SearchBarContainer = styled.div`
  position: relative;
  display: flex;
  align-items: center;
  margin: 10px;
  height: 40px;

  input {
    padding: 8px;
    border: 2px solid #ccc;
    border-radius: 50px;
    flex: 1;
    height: 100%;
    outline: none;
    position: relative;
    text-align: Center;
  }

  .search-icon {
    width: 16px;
    height: 16px;
    position: absolute;
    right: 20px;
    top: 50%;
    transform: translateY(-50%);
    cursor: pointer;
  }
`;

export default function HomeSearchBar() {
  const { search, setSearch, handleSearch } = useSearchState();
  const [isPlaceholderHidden, setPlaceholderHidden] = useState(false);

  const handleClick = () => {
    if (!search) {
      setPlaceholderHidden(true);
      setSearch("");
    }
  };

  return (
    <SearchBarContainer>
      <input
        type="text"
        placeholder={isPlaceholderHidden ? "" : "검색어를 입력하세요"}
        value={search}
        onChange={(e) => setSearch(e.target.value)}
        onClick={handleClick}
      />
      <img
        src={searchImg}
        alt="검색"
        className="search-icon"
        onClick={() => handleSearch(search)}
      />
    </SearchBarContainer>
  );
}
