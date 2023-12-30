package com.api.backend.team.controller;


import com.api.backend.notification.aop.annotation.MemberSendNotifyByTeam;
import com.api.backend.notification.aop.annotation.MembersSendNotifyByTeam;
import com.api.backend.notification.aop.annotation.TeamParticipantsSendNotify;
import com.api.backend.notification.transfers.MemberNotifyByDto;
import com.api.backend.notification.transfers.MembersNotifyByDto;
import com.api.backend.notification.transfers.TeamParticipantsNotifyByDto;
import com.api.backend.team.data.dto.TeamCreateRequest;
import com.api.backend.team.data.dto.TeamCreateResponse;
import com.api.backend.team.data.dto.TeamDisbandRequest;
import com.api.backend.team.data.dto.TeamDisbandResponse;
import com.api.backend.team.data.dto.TeamKickOutRequest;
import com.api.backend.team.data.dto.TeamRestoreResponse;
import com.api.backend.team.data.dto.TeamUpdateRequest;
import com.api.backend.team.data.dto.TeamUpdateResponse;
import com.api.backend.team.data.dto.TeamsDtoResponse;
import com.api.backend.team.service.TeamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "팀")
@RestController
@RequiredArgsConstructor
@RequestMapping("/team")
public class TeamController {

  private final TeamService teamService;

  @ApiOperation(value = "팀의 초대코드 조회")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "팀의 초대코드를 가져왔습니다."),
      @ApiResponse(code = 500, message = "실패")
  })
  @ApiImplicitParams(
      {
          @ApiImplicitParam(
              name = "teamId", value = "팀 id", required = true, dataType = "Long"
              , paramType = "path", defaultValue = "None", example = "1"
          )
      })
  @GetMapping("/{teamId}/code")
  public ResponseEntity<String> getTeamUrlRequest(
      @PathVariable("teamId") Long teamId,
      @ApiIgnore Principal principal
  ) {
    return ResponseEntity.ok(
        teamService.getTeamUrl(teamId, Long.valueOf(principal.getName()))
    );
  }

  @ApiOperation(value = "팀을 생성하는 API", notes = "생성시 생성한 사람이 팀장으로 지정됩니다.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "생성한 팀정보를 반환"),
      @ApiResponse(code = 500, message = "실패")
  })
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<TeamCreateResponse> createTeamRequest(
      @Valid TeamCreateRequest teamRequest,
      @ApiIgnore Principal principal
  ) {
    return ResponseEntity.ok(
        teamService.createTeam(teamRequest, Long.valueOf(principal.getName()))
    );
  }

  @ApiOperation(value = "팀 참가 API", notes = "해당 URL로 접근 한 사람이 팀 참가")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "팀 참가에 대한 정보를 반환"),
      @ApiResponse(
          code = 500,
          message = "팀이 해체 됐거나, 찾을 수 없는 회원인 경우, 팀장이 아닌 경우, 팀장이 팀장 자신을 강퇴하는 경우"
      )
  })
  @ApiImplicitParams(
      {
          @ApiImplicitParam(
              name = "teamId", value = "팀 id", required = true, dataType = "Long"
              , paramType = "path", defaultValue = "None", example = "1"
          ),
          @ApiImplicitParam(
              name = "code", value = "팀 코드", required = true, dataType = "String"
              , paramType = "path", defaultValue = "None", example = "nklndsiofnefm"
          ),
          @ApiImplicitParam(
              name = "expireCode", value = "기한 코드", required = true, dataType = "String"
              , paramType = "path", defaultValue = "None", example = "nklndsiofnefm"
          )
      })
  @TeamParticipantsSendNotify
  @GetMapping("/{teamId}/{code}/{expireCode}")
  public ResponseEntity<TeamParticipantsNotifyByDto> updateTeamParticipantRequest(
      @PathVariable("teamId") Long teamId,
      @PathVariable("code") String code,
      @PathVariable("expireCode") String expireCode ,
      @ApiIgnore Principal principal
  ) {
    return ResponseEntity.ok(
        teamService.updateTeamParticipants(teamId, code, Long.valueOf(principal.getName()), expireCode)
    );
  }

  @ApiOperation(value = "팀원 강퇴 API", notes = "해당 URL로 접근한 사람의 팀 참가")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "생성한 팀정보를 반환"),
      @ApiResponse(code = 500, message = "팀장이 아닌경우, 회원이 없는 경우, 자기 자신을 강퇴하는 경우")
  })
  @PostMapping("/kick-out")
  @MemberSendNotifyByTeam
  public ResponseEntity<MemberNotifyByDto> kickOutTeamParticipantsRequest(
      @RequestBody @Valid
      TeamKickOutRequest teamKickOutRequest,
      @ApiIgnore Principal principal
  ) {
    return ResponseEntity.ok(
        teamService.kickOutTeamParticipants(teamKickOutRequest, Long.valueOf(principal.getName()))
    );
  }

  @ApiOperation(value = "팀원 해체 API", notes = "해당 팀은 팀장만 해체가 가능하다.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "생성한 팀정보를 반환"),
      @ApiResponse(code = 500, message = "권한이 옳바르지 않는 경우, 이미 해체된 경우")
  })
  @PutMapping("/disband")
  @MembersSendNotifyByTeam
  public ResponseEntity<MembersNotifyByDto> disbandTeamRequest(
      @RequestBody @Valid TeamDisbandRequest request,
      @ApiIgnore Principal principal
  ) {
    Long memberId = Long.valueOf(principal.getName());
    return ResponseEntity.ok(
        TeamDisbandResponse.from(
            teamService.disbandTeam(memberId, request),
            memberId)
    );

  }

  @ApiOperation(value = "팀 복구 API", notes = "팀 해체 이후로 30일 이내로 복구가 가능하다.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "생성한 팀정보를 반환"),
      @ApiResponse(code = 500, message = "팀장이 아닌 경우, 팀이 이미 해체된 경우")
  })
  @ApiImplicitParams(
      {
          @ApiImplicitParam(
              name = "teamId", value = "팀 id", required = true, dataType = "Long",
              paramType = "path", defaultValue = "None", example = "1"
          ),
          @ApiImplicitParam(
              name = "restoreDt", value = "팀 복구 날짜(현재 날짜)", required = true, dataType = "LocalDate",
              paramType = "query", defaultValue = "None", example = "2023-01-01"
          )
      })
  @PatchMapping("/{teamId}/restore")
  public ResponseEntity<TeamRestoreResponse> restoreTeamRequest(
      @ApiIgnore Principal principal,
      @RequestParam(value = "restoreDt") @DateTimeFormat(pattern = "yyyy-MM-dd")
      LocalDate restoreDt,
      @PathVariable("teamId") Long teamId
  ) {
    return ResponseEntity.ok(
        TeamRestoreResponse.from(
            teamService.restoreTeam(Long.valueOf(principal.getName()), restoreDt, teamId)
        )
    );
  }

  @ApiOperation(value = "여러 팀 조회 API", notes = "자기 자신을 기준으로 팀을 조회한다.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "생성한 팀정보를 반환"),
      @ApiResponse(code = 500, message = "팀장이 아닌 경우, 팀이 이미 해체된 경우")
  })
  @GetMapping("/list")
  public ResponseEntity<List<TeamsDtoResponse>> getTeamsRequest(
      @ApiIgnore Principal principal
  ) {
    return ResponseEntity.ok(
        teamService.getTeams(Long.valueOf(principal.getName()))
            .stream()
            .map(TeamsDtoResponse::fromByTeamParticipant)
            .collect(Collectors.toList())
    );
  }

  @ApiOperation(value = "팀 수정 API", notes = "자기 자신을 기준으로 팀을 조회한다.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "업데이트된 팀 정보를 반환"),
      @ApiResponse(code = 500, message = "팀장이 아닌 경우, 팀이 이미 해체된 경우")
  })
  @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<TeamUpdateResponse> updateTeamRequest(
      @Valid TeamUpdateRequest teamUpdateRequest,
      @ApiIgnore Principal principal
  ) {
    return ResponseEntity.ok(
        TeamUpdateResponse.from(
            teamService.updateTeam(teamUpdateRequest, Long.valueOf(principal.getName()))
        )
    );
  }

  @ApiOperation(value = "팀 디테일",notes = "팀 조회")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "팀에 대한 세부사항을 알 수 있다."),
      @ApiResponse(code = 500, message = "팀원이 아닌 경우, 팀이 이미 해체된 경우")
  })
  @GetMapping(value = "/{teamId}")
  public ResponseEntity<TeamsDtoResponse> getTeam(
      @PathVariable(value = "teamId") Long teamId,
      @ApiIgnore Principal principal
  ) {
    return ResponseEntity.ok(
        TeamsDtoResponse.from(
            teamService.getTeamByTeamIdAndMemberId(teamId, Long.valueOf(principal.getName()))
        )
    );
  }
}
