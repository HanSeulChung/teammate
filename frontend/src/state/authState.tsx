// authState.ts

import { atom, useRecoilState } from "recoil";
import { useEffect } from "react";

// 로그인 상태를 저장하는 atom
export const isAuthenticatedState = atom({
  key: "isAuthenticatedState",
  default: Boolean(localStorage.getItem("accessToken")),
});

// accessToken을 저장하는 함수
export const saveAccessToken = (token: string | null) => {
  if (token) {
    localStorage.setItem("accessToken", token);
  } else {
    localStorage.removeItem("accessToken");
  }
};

// 로그아웃 함수
export const logout = () => {
  localStorage.removeItem("accessToken");
};

// 홈화면 검색창
export const searchState = atom({
  key: "searchState",
  default: (() => {
    // 로컬 스토리지에서 검색어 가져오기
    const storedSearch = localStorage.getItem("search");
    return storedSearch || "";
  })() as string,
});

export const useSearchState = () => {
  const [search, setSearch] = useRecoilState(searchState);
  const [teamList, setTeamList] = useRecoilState(teamListState);
  const handleSearch = async (searchTerm: string) => {
    console.log("검색어:", searchTerm);

    const searchResults = await fetchTeamsBySearchTeam(searchTerm);
    setTeamList(searchResults);
  };

  const fetchTeamsBySearchTeam = async (searchTeam: string) => {
    const response = await fetch(`/api/teams?search=${searchTeam}`);
    const data = await response.json();
    return data;
  };

  return { search, setSearch, handleSearch, teamList, setTeamList };
};

// 팀 생성창
export const teamNameState = atom({
  key: "teamNameState",
  default: (() => {
    const storedTeamName = localStorage.getItem("teamName");
    return storedTeamName || "";
  })(),
});

export const selectedTeamSizeState = atom({
  key: "selectedTeamSizeState",
  default: (() => {
    const storedTeamSize = localStorage.getItem("selectedTeamSize");
    return storedTeamSize || "1-9";
  })(),
});

export interface Team {
  name: string;
  size: string;
  image: string | null;
}

export const teamListState = atom<Team[]>({
  key: "teamListState",
  default: [], // 초기값을 빈 배열로 설정

  // Recoil 초기화 함수 사용
  effects_UNSTABLE: [
    ({ setSelf }) => {
      // 로컬 스토리지에서 팀 목록 가져오기
      const storedTeamList = localStorage.getItem("teamList");
      if (storedTeamList) {
        setSelf(JSON.parse(storedTeamList));
      }
    },
  ],
});

// 마이페이지 (사용자 정보)
export interface User {
  id: string;
  name: string;
}

export const userState = atom<User | null>({
  key: "userState",
  default: null, // 직접 값을 지정
  effects_UNSTABLE: [
    ({ onSet }) => {
      // 아톰 값이 변경될 때 로컬 스토리지에 사용자 정보 저장
      onSet((newValue) => {
        localStorage.setItem("user", JSON.stringify(newValue));
      });
    },
  ],
});

export const useUser = () => {
  const [user, setUser] = useRecoilState(userState);

  const saveUser = (loggedInUser: User) => {
    setUser(loggedInUser);
    // 사용자 정보를 localStorage에 저장
    localStorage.setItem("user", JSON.stringify(loggedInUser));
  };

  useEffect(() => {
    // 앱이 로드될 때 사용자 정보를 localStorage에서 복구
    const storedUser = localStorage.getItem("user");
    if (storedUser) {
      setUser(JSON.parse(storedUser));
    }
  }, [setUser]);

  return { user, setUser, saveUser };
};
