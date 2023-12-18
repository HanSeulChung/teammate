package com.api.backend.notification.controller;


import com.api.backend.notification.service.EmitterService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
public class NotificationController {

  private final EmitterService emitterService;

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
}
