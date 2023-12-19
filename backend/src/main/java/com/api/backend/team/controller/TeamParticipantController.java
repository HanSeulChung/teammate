package com.api.backend.team.controller;

import static com.api.backend.team.data.ResponseMessage.DELETE_TEAM_PARTICIPANT;

import com.api.backend.global.aop.notify.SendNotify;
import com.api.backend.notification.data.dto.DtoValueExtractor;
import com.api.backend.team.data.dto.TeamParticipantsDeleteResponse;
import com.api.backend.team.data.dto.TeamParticipantsDto;
import com.api.backend.team.data.entity.TeamParticipants;
import com.api.backend.team.service.TeamParticipantsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "팀 참가자")
@RestController
@RequiredArgsConstructor
@RequestMapping("/team/{teamId}/participant")
public class TeamParticipantController {

  private final TeamParticipantsService teamParticipantsService;

  @ApiOperation(value = "팀 탈퇴 API")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "팀에서 탈퇴 되었다는 메시지를 반환"),
      @ApiResponse(code = 500, message = "팀원이 아닌 경우, 팀장인 경우")
  })
  @ApiImplicitParams(
      {
          @ApiImplicitParam(
              name = "teamId", value = "팀 id", required = true, dataType = "Long"
              , paramType = "path", defaultValue = "None", example = "1"
          )
      })
  @DeleteMapping
  @SendNotify
  public ResponseEntity<DtoValueExtractor> deleteTeamParticipantRequest(
      @ApiIgnore Principal principal,
      @PathVariable(value = "teamId") Long teamId
  ) {
    TeamParticipants teamParticipants = teamParticipantsService
        .deleteTeamParticipant(Long.valueOf(principal.getName()), teamId);

    return ResponseEntity.ok(
        TeamParticipantsDeleteResponse
            .builder()
            .teamId(teamId)
            .nickName(teamParticipants.getTeamNickName())
            .teamParticipantsId(teamParticipants.getTeamParticipantsId())
            .message(DELETE_TEAM_PARTICIPANT)
            .build()
    );
  }

  @ApiOperation(value = "팀장 위임 API")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "성공적으로 팀장 위임이 됐다는 메시지"),
      @ApiResponse(code = 500,
          message = "팀원이 아닌 경우, 팀장인 경우, 위임 대상이 팀원이 아닌 경우,같은 팀이 아닌 경우"
      )
  })
  @ApiImplicitParams(
      {
          @ApiImplicitParam(
              name = "teamId", value = "팀 id", required = true, dataType = "Long"
              , paramType = "path", defaultValue = "None", example = "1"
          ),
          @ApiImplicitParam(
              name = "participantId", value = "팀 참가자 id", required = true, dataType = "Long"
              , paramType = "path", defaultValue = "None", example = "1"
          )
      })
  @PatchMapping("/{participantId}")
  public ResponseEntity<String> updateRoleTeamParticipantRequest(
      @ApiIgnore Principal principal,
      @PathVariable(value = "participantId") Long participantId,
      @PathVariable(value = "teamId") Long teamId
  ) {
    return ResponseEntity.ok(
        teamParticipantsService.updateRoleTeamParticipant(Long.valueOf(principal.getName()),
            participantId, teamId)
    );
  }

  @ApiOperation(value = "여러 팀원 조회 API")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "팀원을 반환")
  })
  @ApiImplicitParams(
      {
          @ApiImplicitParam(
              name = "teamId", value = "팀 id", required = true, dataType = "Long"
              , paramType = "path", defaultValue = "None", example = "1"
          )
      })
  @GetMapping("/list")
  public ResponseEntity<List<TeamParticipantsDto>> getTeamParticipantsRequest(
      @PathVariable(value = "teamId") Long teamId,
      @ApiIgnore Principal principal
  ) {
    return ResponseEntity.ok(
        teamParticipantsService.getTeamParticipants(teamId, Long.valueOf(principal.getName()))
            .stream().map(TeamParticipantsDto::from)
            .collect(Collectors.toList())
    );
  }

  @ApiOperation(value = "단일 팀 참가자 조회 API")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "팀원을 반환"),
      @ApiResponse(code = 500, message = "팀원이 아닌 경우, 팀이 해체된 경우")
  })
  @ApiImplicitParams(
      {
          @ApiImplicitParam(
              name = "teamId", value = "팀 id", required = true, dataType = "Long"
              , paramType = "path", defaultValue = "None", example = "1"
          )
      })
  @GetMapping
  public ResponseEntity<TeamParticipantsDto> getTeamParticipantRequest(
      @PathVariable(value = "teamId") Long teamId,
      @ApiIgnore Principal principal
  ) {
    return ResponseEntity.ok(
        TeamParticipantsDto.from(
            teamParticipantsService.getTeamParticipant(teamId, Long.valueOf(principal.getName()))
        )
    );
  }
}
