import { atom } from 'recoil';

export const isAuthenticatedState = atom({
  key: 'isAuthenticatedState',
  default: false, // 기본값은 로그인되어 있지 않음
});