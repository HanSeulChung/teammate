import { atom, useRecoilState, selector } from "recoil";
import { recoilPersist } from "recoil-persist";
import { useEffect } from "react";
import { Team, User, TokenState } from "../interface/interface";

const { persistAtom } = recoilPersist();

//로그인된 사용자 상태
export const loggedInUserState = atom<string | null>({
  key: "loggedInUserState",
  default: null,
});

// //리더인 팀 상태 선택기 (위에 string 을 User로 바꾸면됨)
// export const teamsByLeaderState = selector({
//   key: "teamsByLeaderState",
//   get: ({ get }) => {
//     const userTeams = get(userTeamsState);
//     const loggedInUser = get(loggedInUserState);

//     if (!loggedInUser) {
//       return [];
//     }

//     // 로그인한 사용자가 리더인 팀만 반환
//     return userTeams.filter((team) => team.leaderId === loggedInUser.id);
//   },
// });

// 인증 상태
export const isAuthenticatedState = atom({
  key: "isAuthenticatedState",
  default: false,
  effects_UNSTABLE: [persistAtom],
});

// 액세스 토큰 상태
export const accessTokenState = atom({
  key: "accessToken",
  default: "",
  // effects_UNSTABLE: [persistAtom],
});

// 리프레시 토큰 상태
export const refreshTokenState = atom({
  key: "refreshToken",
  default: "",
  // effects_UNSTABLE: [persistAtom],
});

// 사용자 정보 상태
export const userState = atom({
  key: "userState",
  default: null as { id: string; name: string } | null,
  // effects_UNSTABLE: [persistAtom],
});

// 리프레시 토큰 저장 함수
export const saveRefreshToken = (token: string | null) => {
  if (token) {
    localStorage.setItem("refreshToken", token);
  } else {
    localStorage.removeItem("refreshToken");
  }
};

// 액세스 토큰 저장 함수
export const saveAccessToken = (token: string | null) => {
  if (token) {
    localStorage.setItem("accessToken", token);
  } else {
    localStorage.removeItem("accessToken");
  }
};

// 토큰 저장 함수
export const saveToken = ({ accessToken, refreshToken }: TokenState) => {
  localStorage.setItem("accessToken", accessToken);
  localStorage.setItem("refreshToken", refreshToken);
};

// 로그아웃 함수
export const logout = () => {
  saveToken({ accessToken: "", refreshToken: "" });
};

// 홈 화면 검색 상태
export const searchState = atom({
  key: "searchState",
  // default: (() => {
  //   const storedSearch = localStorage.getItem("search");
  //   return storedSearch || "";
  // })() as string,
  default: localStorage.getItem("search") || "",
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
  key: "userInfoState",
  default: { teams: [] },
});

export const userTeamsState = atom<Team[]>({
  key: "userTeamsState",
  default: [],
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
// export const selectedTeamState = atom({
//   key: "selectedTeam",
//   default: "",
// });
export const selectedTeamState = atom<string | null>({
  key: "selectedTeamState",
  default: null,
});

// 선택된 팀 상태 관련 함수
export const useSelectedTeamState = () => useRecoilState(selectedTeamState);

// export const userState = atom<User | null>({
//   key: "userState",
//   default: null,

//   effects_UNSTABLE: [
//     ({ onSet }) => {
//       onSet((newValue) => {
//         localStorage.setItem("user", JSON.stringify(newValue));
//       });
//     },
//   ],
// });

export const useUser = () => {
  const [user, setUser] = useRecoilState(userState);

  const saveUser = (loggedInUser: User) => {
    setUser(loggedInUser);
    localStorage.setItem("user", JSON.stringify(loggedInUser));
  };

  useEffect(() => {
    const storedUser = localStorage.getItem("user");

    if (storedUser) {
      try {
        const parsedUser = JSON.parse(storedUser);
        setUser(parsedUser);
      } catch (error) {
        console.error("Error parsing user from localStorage:", error);
      }
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
