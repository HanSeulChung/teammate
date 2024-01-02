//팀 생성 시
export interface TeamInfoData {
  teamName: string;
  memberLimit: number;
  code: string;
  inviteLink: string;
  teamImg: string;
}

// 팀 생성 후 정보 인터페이스
export interface Team {
  name: string;
  teamId: number;
  profileUrl: string | null;
  leaderId: number | null;
  nickname?: string | null;
  members?: User[];
  code: string;
  memberLimit: number;
  inviteLink: string;
  teamName:string;
  restorationDt:null;
  teamRole: string;
  teamParticipantsId:number;
  participantsProfileUrl:string|undefined;
  teamNickName:string;
}

// 사용자 정보 인터페이스
export interface User {
  email: string;
  id: string;
  name: string;
  loginType:string;
}

// 토큰 상태 인터페이스
export interface TokenState {
  accessToken: string;
  refreshToken: string;
}

//내 팀 프로필
export interface TeamProfileUpdate {
  teamParticipantsId: number;
  teamId: number;
  teamRole: string;
  participantsProfileUrl: string;
  teamNickName: string;
}

//멤버
export interface TeamParticipant {
  teamParticipantsId: number;
  teamRole: string;
  participantsProfileUrl: string;
  teamNickName: string;
}

// 카테고리 목록
export interface ICategoryList {
  categoryId: number;
  categoryName: string;
}

// 일정목록 렌더링을 위한 타입
export interface ConvertedEvent {
  id: number;
  start: string;
  end: string;
  title: string;
  borderColor: string;
  backgroundColor: string;
  extendedProps: {
    content: string;
    place: string;
    scheduleType: string;
    category: string;
    categoryName: string;
    categoryId: number,
  };
}