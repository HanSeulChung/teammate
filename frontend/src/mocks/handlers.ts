//이 핸들러를 활용하면 됨.
import { rest } from "msw";
import people from "./dummy.json";

// 캘린더 테스트용 임시 데이터(db)
const calendarSchedules = [
    { id: "1", title: "Meeting1", start: new Date('2023-11-29') },
    { id: "2", title: 'Meeting2', start: new Date('2023-11-30'), contents: "백프로팀 회의 하는 날" },
    { id: "3", title: 'Meeting3', start: new Date('2023-11-30'), contents: "화분 물 주는 날" },
    { id: "4", title: 'msw일정1', start: new Date('2023-12-06 10:20:20'), contents: "대청소 하는 날", place: "자택"},
    { id: "5", title: 'msw일정2', start: new Date('2023-12-06 10:00:20'), daysOfWeek:[2,4], extendedProps: { contents: "친구 만나는 날", place: "서울특별시", groupId: "주간회의"}}
]

export const handlers = [
  rest.get("/people", async (req, res, ctx) => {
    await sleep(200);
    return res(ctx.status(200), ctx.json(people));
  }),
//   rest.get("/*", async (req, res, ctx) => {
//     return res(
//       ctx.status(200),
//       ctx.json({ message: "Mocked response for all GET requests" }),
//     );
//   }),
  rest.post("/sign-up", (req, res, ctx) => {
    return res(ctx.status(200), ctx.json({ message: "가입 성공" }));
  }),

  // 달력 일정 불러오기
  rest.get("/schedules", (req, res, ctx) => {
    return res (
        ctx.status(200),
        ctx.json(calendarSchedules)
    );
  }),

  // 달력 일정 추가
  rest.post("/schedules", async (req, res, ctx) => {
    // 요청 본문 json 받기
    const event = await req.json();

    // 아이디 할당
    event.id = calendarSchedules.length + 1;

    // 이벤트를 일정목록 db(배열)에 추가
    calendarSchedules.push(event);

    return res(
        ctx.status(201),
        ctx.json({
            calendarSchedules
        })
    );
  }),
];

async function sleep(timeout: number) {
  return new Promise((resolve) => {
    setTimeout(resolve, timeout);
  });
}
