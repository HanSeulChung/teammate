import React, { useState } from "react";
import { useSearchState } from "../../state/authState";
import styled from "styled-components";
import searchImg from "../../assets/search.png";

const SearchBarContainer = styled.div`
  position: absolute;
  top: 150px;
  display: flex;
  align-items: center;
  width: 1000px;
  height: 50px;

  input {
    padding: 8px;
    border: 2px solid #ccc;
    border-radius: 50px;
    flex: 1;
    height: 100%;
    outline: none;
    position: relative;
    text-align: Center;
    background: white;
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
  const handleFocus = () => {
    setPlaceholderHidden(true);
  };

  const handleBlur = () => {
    if (!search) {
      setPlaceholderHidden(false);
    }
  };

  return (
    <SearchBarContainer>
      <input
        type="text"
        placeholder={isPlaceholderHidden ? "" : "팀 명을 검색하세요"}
        value={search}
        onChange={(e) => setSearch(e.target.value)}
        onClick={handleClick}
        onFocus={handleFocus}
        onBlur={handleBlur}
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
