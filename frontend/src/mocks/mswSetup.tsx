import { rest } from "msw";
import { setupServer } from 'msw/node';

interface TeamRequestBody {
  teamName: string;
  size: string;
  image: string;
  leaderId: string;
}

export const server = setupServer(
  // 팀 생성을 위한 Mock API 엔드포인트
  rest.post('/team', (req, res, ctx) => {
    const { teamName, size, image, leaderId }: TeamRequestBody = req.body as TeamRequestBody;

    // 가짜로 생성된 데이터
    const teamId = Math.floor(Math.random() * 1000) + 1;
    const generatedLeaderId = Math.floor(Math.random() * 100) + 1;
    const code = `http://example.com/invitation/${teamId}`;

    return res(
      ctx.json({
        teamId,
        teamName,
        leaderId: generatedLeaderId.toString(),
        code,
      })
    );
  })
);