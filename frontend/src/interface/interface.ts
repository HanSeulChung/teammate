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
}

// 사용자 정보 인터페이스
export interface User {
  // userId: string;
  id: string;
  name: string;
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
