import { atom, useRecoilState } from "recoil";

// 로그인 상태를 저장하는 atom
export const isAuthenticatedState = atom({
  key: "isAuthenticatedState",
  default: Boolean(localStorage.getItem("accessToken")), // 로컬 스토리지에 accessToken이 있는지 여부에 따라 초기값 설정
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

//홈화면 검색창
export const searchState = atom({
  key: "searchState",
  default: "",
});

//팀 생성창
export const teamNameState = atom({
  key: "teamNameState",
  default: "",
});

export const selectedTeamSizeState = atom({
  key: "selectedTeamSizeState",
  default: "1-9",
});

export interface Team {
  name: string;
  size: string;
  image: string | null;
}

export const teamListState = atom<Team[]>({
  key: "teamListState",
  default: [],
});

//마이페이지 (사용자 정보)
export interface User {
  id: string;
  name: string;
  // password: string;
}

export const userState = atom<User | null>({
  key: "userState",
  default: null,
});

export const useUser = () => {
  const [user, setUser] = useRecoilState(userState);

  const saveUser = (loggedInUser: User) => {
    setUser(loggedInUser);
    // 사용자 정보를 localStorage에 저장
    localStorage.setItem("user", JSON.stringify(loggedInUser));
  };

  return { user, setUser, saveUser };
};