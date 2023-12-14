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
  id: string;
  name: string;
  size: string;
  image: string | null;
  leaderId: string | null;
  nickname?: string | null;
  members?: User[];
}

// 사용자 정보 인터페이스
export interface User {
  id: string;
  name: string;
}

// 토큰 상태 인터페이스
export interface TokenState {
  accessToken: string;
  refreshToken: string;
}

export interface TeamProfileProps {
  selectedTeam: string | null;
  selectedImage: string | null;
  nickname: string;
  handleImageUpload: (e: React.ChangeEvent<HTMLInputElement>) => void;
  handleNicknameChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  handleUpdateProfile: (image: string | null, nickname: string) => void;
  teamList: Team[];
}
