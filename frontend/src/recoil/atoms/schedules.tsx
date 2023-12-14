// import { atom } from 'recoil';

// export interface ISchedulesTypes {
//     id: string;
//     title: string;
//     start: Date;
//     extendedProps: {
//         contents: string;
//         place?: string;
//         groupId?: string;
//     };
// }

// // recoil state 생성
// export const schedules = atom<ISchedulesTypes[]>({
//     key: 'content',
//     default: [
//         { id: "1", title: "Meeting1", start: new Date('2023-11-29'), extendedProps: { contents: "" } },
//         { id: "2", title: 'Meeting2', start: new Date('2023-11-30'),  extendedProps: { contents: "백프로팀 회의 하는 날"} },
//         { id: "3", title: 'Meeting3', start: new Date('2023-11-30'),  extendedProps: { contents: "화분 물 주는 날"} },
//         { id: "4", title: 'msw일정1', start: new Date('2023-12-06T10:20:20'), extendedProps: { contents: "대청소 하는 날", place: "자택"}},
//         { id: "5", title: 'msw일정2', start: new Date('2023-12-06T10:00:20'), extendedProps: { contents: "친구 만나는 날", place: "서울특별시", groupId: "주간회의"}},
//         // { id: "6", title: 'msw일정3', start: new Date('2023-12-11T10:00:20'), extendedProps: { contents: "여권 수령일", place: "서울특별시", groupId: "주간회의"}},
//     ]
// });
