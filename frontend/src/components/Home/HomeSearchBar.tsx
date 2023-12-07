import React from "react";
import { useRecoilState } from "recoil";
import { searchState } from "../../state/authState";

export default function HomeSearchBar() {
  const [searchTerm, setSearchTerm] = useRecoilState(searchState);

  const handleSearch = () => {
    // 검색을 수행하거나 다른 작업을 수행할 수 있습니다.
    console.log("검색어:", searchTerm);
  };

  return (
    <div>
      <input
        type="text"
        placeholder="검색어를 입력하세요"
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
      />
      <button onClick={handleSearch}>검색</button>
    </div>
  );
}
