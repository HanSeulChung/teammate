package com.api.backend.global.scheduler;

import com.api.backend.member.data.entity.Member;
import com.api.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberSchedulerService {

    private final MemberService memberService;
    @Scheduled(cron = "0 0 5 1 * *", zone = "Asia/Seoul")
    public void checkEmailValidation() {
        log.info("장기간 이메일 안된 회원 삭제");
        List<Member> membersIsAuthenticatedEmailFalse = memberService.getMembersIsAuthenticatedEmailFalse(false, LocalDateTime.now());

        for(Member member: membersIsAuthenticatedEmailFalse){
            log.info(member.toString());
            memberService.deleteMember(member);
        }
    }

}
