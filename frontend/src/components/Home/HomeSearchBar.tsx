import React from "react";
import { useSearchState } from "../../state/authState";

export default function HomeSearchBar() {
  const { search, setSearch, handleSearch } = useSearchState();

  return (
    <div>
      <input
        type="text"
        placeholder="검색어를 입력하세요"
        value={search}
        onChange={(e) => setSearch(e.target.value)}
      />
      <button onClick={() => handleSearch(search)}>검색</button>
    </div>
  );
}
