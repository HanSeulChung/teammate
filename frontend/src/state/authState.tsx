import { atom, useRecoilState, selector } from "recoil";
import { recoilPersist } from 'recoil-persist';
import { useEffect } from "react";
const { persistAtom } = recoilPersist();

// 사용자 정보
export interface User {
  id: string;
}

export const loggedInUserState = atom<User | null>({
  key: "loggedInUserState",
  default: null,
});

export const teamsByLeaderState = selector({
  key: "teamsByLeaderState",
  get: ({ get }) => {
    const userTeams = get(userTeamsState);
    const loggedInUser = get(loggedInUserState);

    if (!loggedInUser) {
      return [];
    }

    // 로그인한 사용자가 리더인 팀만 반환
    return userTeams.filter((team) => team.leaderId === loggedInUser.id);
  },
});

// 팀 목록 
export interface Team {
  id: string;
  name: string;
  size: string;
  image: string | null;
  leaderId: string | null;
  nickname?: string | null;
  members?:User[] // members 속성 추가
}

// 인증 상태
export const isAuthenticatedState = atom({
  key: "isAuthenticatedState",
  default: Boolean(sessionStorage.getItem("accessToken")),
});

// 액세스 토큰 상태
export const accessTokenState = atom({
  key: "accessToken",
  default: "",
});

// 리프레시 토큰 상태
export const refreshTokenState = atom({
  key: "refreshToken",
  default: "",
});

// 리프레시 토큰 저장 함수
export const saveRefreshToken = (token: string | null) => {
  if (token) {
    sessionStorage.setItem("refreshToken", token);
  } else {
    sessionStorage.removeItem("refreshToken");
  }
};

// 액세스 토큰 저장 함수
export const saveAccessToken = (token: string | null) => {
  if (token) {
    sessionStorage.setItem("accessToken", token);
  } else {
    sessionStorage.removeItem("accessToken");
  }
};

// 로그아웃 함수
export const logout = () => {
  sessionStorage.removeItem("accessToken");
};

// 홈 화면 검색 상태
export const searchState = atom({
  key: "searchState",
  default: (() => {
    const storedSearch = localStorage.getItem("search");
    return storedSearch || "";
  })() as string,
});

// 검색 관련 상태 및 함수
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

// 사용자 정보 상태
export const userInfoState = atom({
  key: 'userInfoState',
  default: { teams: [] }, 
});

//팀 목록 상태
export const teamListState = atom<Team[]>({
  key: "teamListState",
  default: [],

  effects_UNSTABLE: [persistAtom],
});


// 팀 생성 상태
export const teamNameState = atom({
  key: "teamNameState",
  default: (() => {
    const storedTeamName = localStorage.getItem("teamName");
    return storedTeamName || "";
  })(),
});

// 팀 목록 상태
export const initialTeams: Team[] = [
  {
    id: "1",
    name: "Team A",
    size: "1-9",
    image: null,
    leaderId: "user1",
    members: [{ id: "user1", name: "User 1" }, { id: "user2", name: "User 2" }],
  },
  {
    id: "2",
    name: "Team B",
    size: "10-99",
    image: null,
    leaderId: "user2",
    members: [{ id: "user2", name: "User 2" }, { id: "user4", name: "User 4" }],
  },
  {
    id: "3",
    name: "Team c",
    size: "100",
    image: null,
    leaderId: "user3",
    members: [],
  },
  // ... 다른 팀들
];

export const userTeamsState = atom<Team[]>({
  key: "userTeamsState",
  default: initialTeams,
});

//선택된 팀 크기 상태
export const selectedTeamSizeState = atom({
  key: "selectedTeamSizeState",
  default: (() => {
    const storedTeamSize = localStorage.getItem("selectedTeamSize");
    return storedTeamSize || "1-9";
  })(),
});

// 마이페이지 선택된 팀 상태
export const selectedTeamState = atom({
  key: "selectedTeam",
  default: "",
});

// 선택된 팀 상태 관련 함수
export const useSelectedTeamState = () => useRecoilState(selectedTeamState);

// 사용자 정보 및 함수
export interface User {
  id: string;
  name: string;
}

export const userState = atom<User | null>({
  key: "userState",
  default: null,

  effects_UNSTABLE: [
    ({ onSet }) => {
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
    localStorage.setItem("user", JSON.stringify(loggedInUser));
  };

  useEffect(() => {
    const storedUser = localStorage.getItem("user");
    if (storedUser) {
      setUser(JSON.parse(storedUser));
    }
  }, [setUser]);

  return { user, setUser, saveUser };
};

// 마이페이지 선택된 팀 정보 상태
export const selectedTeamInfoState = atom({
  key: "selectedTeamInfoState",
  default: {
    name: "",
    image: "",
    nickname: "",
  },
});

