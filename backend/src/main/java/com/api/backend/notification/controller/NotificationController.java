package com.api.backend.notification.controller;


import com.api.backend.notification.data.dto.NotificationsResponse;
import com.api.backend.notification.service.EmitterService;
import com.api.backend.notification.service.NotificationService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

  private final EmitterService emitterService;
  private final NotificationService notificationService;

  @ApiOperation(value = "팀 알림 구독 API",notes = "emitter를 반환한다.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "업데이트된 팀 정보를 반환"),
      @ApiResponse(code = 500, message = "팀원이 아닌 경우")
  })
  @ApiImplicitParams(
      {
          @ApiImplicitParam(
              name = "teamId", value = "팀 id", required = true, dataType = "Long",
              paramType = "path", defaultValue = "None", example = "1"
          )
      })
  @GetMapping(value = "/subscribe/team/{teamId}", produces = "text/event-stream")
  public SseEmitter subscribeTeamRequest(
      @ApiIgnore Principal principal,
      @PathVariable(value = "teamId") Long teamId
  ) {
    return emitterService.setTeamParticipantEmitter(
        teamId, Long.valueOf(principal.getName())
    );
  }

  @ApiOperation(value = "맴버 알림 구독 API",notes = "emitter를 반환한다.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "업데이트된 팀 정보를 반환"),
      @ApiResponse(code = 500, message = "맴버가 아닌 경우")
  })
  @GetMapping(value = "/subscribe/member", produces = "text/event-stream")
  public SseEmitter subscribeMemberRequest(
      @ApiIgnore Principal principal
  ) {
    return emitterService.setMemberEmitter(Long.valueOf(principal.getName()));
  }

  @ApiOperation(value = "팀 알림 list API",notes = "팀에 관한 알람을 받을 수 있다.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "page처리된 dto를 받을 수 있다."),
      @ApiResponse(code = 500, message = "팀원이 아닌 경우, 팀이 해체된 경우")
  })
  @ApiImplicitParams(
      {
          @ApiImplicitParam(
              name = "teamId", value = "팀 id", required = true, dataType = "Long",
              paramType = "path", defaultValue = "None", example = "1"
          )
      })

  @GetMapping(value = "/team/{teamId}")
  public ResponseEntity<Page<NotificationsResponse>> getTeamNotificationListRequest(
      @ApiIgnore Principal principal,
      @PathVariable(value = "teamId") Long teamId,
      Pageable pageable
  ) {
    return ResponseEntity.ok(
        NotificationsResponse.fromDtos(
            notificationService.getTeamNotificationList(
                teamId,
                Long.valueOf(principal.getName()),
                pageable
            )
        )
    );
  }

  @ApiOperation(value = "맴버 알림 list API",notes = "맴버에 관한 알람을 받을 수 있다.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "page처리된 dto를 받을 수 있다.")
  })
  @GetMapping(value = "/member")
  public ResponseEntity<Page<NotificationsResponse>> getMemberNotificationListRequest(
      @ApiIgnore Principal principal,
      Pageable pageable
  ) {
    return ResponseEntity.ok(
        NotificationsResponse.fromDtos(
            notificationService.getMemberNotificationList(
                Long.valueOf(principal.getName()),
                pageable
            )
        )
    );
  }

  @ApiOperation(value = "맴버 알람 read api")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "업데이트된 팀 정보를 반환"),
      @ApiResponse(code = 500, message = "알람이 존재하지 않는 경우, 알람을 읽을 권한이 없는 경우")
  })
  @ApiImplicitParams(
      {
          @ApiImplicitParam(
              name = "notificationId", value = "알람 id", required = true, dataType = "Long",
              paramType = "path", defaultValue = "None", example = "1"
          )
      })
  @PutMapping(value = "/{notificationId}/member")
  public ResponseEntity<Void> notificationMemberReadRequest(
      @ApiIgnore Principal principal,
      @PathVariable(value = "notificationId") Long notificationId
  ) {
    notificationService.memberReadNotification(notificationId, Long.valueOf(principal.getName()));

    return ResponseEntity.ok().build();
  }

  @ApiOperation(value = "팀 참가자 알람 read api")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "업데이트된 팀 정보를 반환"),
      @ApiResponse(code = 500, message = "알람이 존재하지 않는 경우, 알람을 읽을 권한이 없는 경우")
  })
  @ApiImplicitParams(
      {
          @ApiImplicitParam(
              name = "notificationId", value = "알람 id", required = true, dataType = "Long",
              paramType = "path", defaultValue = "None", example = "1"
          ),
          @ApiImplicitParam(
              name = "teamParticipantsId", value = "팀 참가자 id", required = true, dataType = "Long",
              paramType = "path", defaultValue = "None", example = "1"
          )
      })
  @PutMapping(value = "/{notificationId}/teamParticipants/{teamParticipantsId}")
  public ResponseEntity<Void> notificationTeamReadRequest(
      @ApiIgnore Principal principal,
      @PathVariable(value = "teamParticipantsId") Long teamParticipantsId,
      @PathVariable(value = "notificationId") Long notificationId
  ) {
    notificationService
        .teamParticipantsReadNotification(
            notificationId,
            teamParticipantsId,
            Long.valueOf(principal.getName())

        );

    return ResponseEntity.ok().build();
  }
}
