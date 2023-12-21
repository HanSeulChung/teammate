package com.api.backend.notification.aop;

import com.api.backend.member.data.entity.Member;
import com.api.backend.notification.data.dto.NotificationDto;
import com.api.backend.notification.data.entity.Notification;
import com.api.backend.notification.service.EmitterService;
import com.api.backend.notification.service.NotificationService;
import com.api.backend.notification.service.SendNotificationService;
import com.api.backend.notification.transfers.MemberNotifyByDto;
import com.api.backend.notification.transfers.MembersNotifyByDto;
import com.api.backend.notification.transfers.MentionTeamParticipantsNotifyByDto;
import com.api.backend.notification.transfers.TeamParticipantsNotifyByDto;
import com.api.backend.team.data.entity.TeamParticipants;
import com.api.backend.team.service.TeamParticipantsService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Aspect
@Slf4j
@Component
@EnableAsync
@RequiredArgsConstructor
public class NotifyAop {

  private final SendNotificationService sendNotificationService;
  private final TeamParticipantsService teamParticipantsService;
  private final NotificationService notificationService;
  private final EmitterService emitterService;

  @Pointcut("@annotation(com.api.backend.notification.aop.annotation.MemberSendNotifyByTeam)")
  public void memberPointCut() {
  }

  @Pointcut("@annotation(com.api.backend.notification.aop.annotation.MembersSendNotifyByTeam)")
  public void membersPointCut() {
  }
  @Pointcut("@annotation(com.api.backend.notification.aop.annotation.TeamParticipantsSendNotify)")
  public void teamParticipantsPointCut() {
  }
  @Pointcut("@annotation(com.api.backend.notification.aop.annotation.MentionTeamParticipantsSendNotify)")
  public void mentionTeamParticipantsPointCut() {
  }

  @Async
  @AfterReturning(pointcut = "memberPointCut()", returning = "result")
  public void memberCheckNotify(JoinPoint joinPoint, ResponseEntity<MemberNotifyByDto> result) {
    log.info("member 알람 진입");
    MemberNotifyByDto info = result.getBody();

    Notification notification = Notification
        .convertToMemberNotify(
            info.getMemberId()
            , info.getTeamName()
            , info.getSendMessage()
            , info.getAlarmType()
        );

    SseEmitter emitter = emitterService.getMemberEmitter(info.getMemberId());

    if (emitter == null) {
      log.info("emitter null");
      return ;
    }

    emitterService.sendNotification(emitter, notification);
    log.info("member 알람전송 종료");

    notificationService.saveNotification(notification);
    log.info("member 알람저장 종료");
  }

  @Async
  @AfterReturning(pointcut = "membersPointCut()", returning = "result")
  public void membersCheckNotify(JoinPoint joinPoint, ResponseEntity<MembersNotifyByDto> result) {
    log.info("members 알람 진입");
    MembersNotifyByDto info = result.getBody();

    List<Member> members = sendNotificationService
        .getSendMembers(info.getTeamId(), info.getExcludeMemberId());

    if (members.isEmpty()) {
      log.info("members null");
      return ;
    }

    List<Notification> notifications = members
        .stream().map(i ->
            Notification.convertToMemberNotify(
                i,
                info.getTeamName(),
                info.getSendMessage(),
                info.getAlarmType()
            )
        )
        .collect(Collectors.toList());

    sendNotificationService.sendNotifications(
        sendNotificationService.getMemberEmitters(members),
        NotificationDto.from(notifications.get(0))
    );

    log.info("members 알람전송 종료");

    notificationService.saveAllNotification(notifications);
    log.info("members 알람저장 종료");

  }

  @Async
  @AfterReturning(pointcut = "teamParticipantsPointCut()", returning = "result")
  public void teamParticipantsCheckNotify(JoinPoint joinPoint, ResponseEntity<TeamParticipantsNotifyByDto> result) {
    log.info("teamParticipants 알람 진입");
    TeamParticipantsNotifyByDto info = result.getBody();

    String nickName = info.getTeamParticipantsNickName();

    List<TeamParticipants> teamParticipants = teamParticipantsService
        .getTeamParticipantsExcludeId(
            info.getExcludeTeamParticipantId(), info.getTeamId()
        );

    if (teamParticipants.isEmpty()) {
      log.info("teamParticipants null");
      return ;
    }
    List<Notification> notifications = teamParticipants
        .stream().map(i ->
            Notification.convertNickNameToTeamParticipantsNotify(
                i,
                nickName,
                info.getSendMessage(),
                info.getAlarmType()
            )
        )
        .collect(Collectors.toList());

    sendNotificationService.sendNotifications(
        sendNotificationService.getTeamEmitters(info.getTeamId(), teamParticipants),
        NotificationDto.from(notifications.get(0))
    );

    log.info("teamParticipants 알람전송 종료");

    notificationService.saveAllNotification(notifications);

    log.info("teamParticipants 알람저장 종료");
  }

  @Async
  @AfterReturning(pointcut = "mentionTeamParticipantsPointCut()", returning = "result")
  public void mentionTeamParticipantsCheckNotify(JoinPoint joinPoint, ResponseEntity<MentionTeamParticipantsNotifyByDto> result) {

  }


}
