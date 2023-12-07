import React from "react";
import { useRecoilState } from "recoil";
import {
  searchState,
  teamListState,
  useSearchState,
} from "../../state/authState";

export default function HomeSearchBar() {
  const { search, setSearch, handleSearch } = useSearchState();
  // const [searchTeam, setSearchTeam] = useRecoilState(searchState);
  // const setTeamList = useRecoilState(teamListState)[1];

  // const handleSearch = async () => {
  //   // 검색을 수행하거나 다른 작업을 수행할 수 있습니다.
  //   console.log("검색어:", searchTeam);

  //   const searchResults = await fetchTeamsBySearchTeam(searchTeam);
  //   setTeamList(searchResults);
  // };

  // // 실제 서버와 통신하여 검색 결과를 가져오는 함수
  // const fetchTeamsBySearchTeam = async (searchTeam: string) => {
  //   const response = await fetch(`/api/teams?search=${searchTeam}`);
  //   const data = await response.json();

  //   return data;
  // };

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
