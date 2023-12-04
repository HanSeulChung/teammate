//이 핸들러를 활용하면 됨.
import { rest } from "msw";
import people from "./dummy.json";

export const handlers = [
  rest.get("/people", async (req, res, ctx) => {
    await sleep(200);
    return res(ctx.status(200), ctx.json(people));
  }),
  rest.get("/*", async (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json({ message: "Mocked response for all GET requests" })
    );
  }),
];

async function sleep(timeout: number) {
  return new Promise((resolve) => {
    setTimeout(resolve, timeout);
  });
}
