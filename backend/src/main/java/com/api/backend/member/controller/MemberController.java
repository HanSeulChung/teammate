package com.api.backend.member.controller;

import com.api.backend.global.security.AuthService;
import com.api.backend.global.security.data.dto.TokenDto;
import com.api.backend.member.data.dto.*;
import com.api.backend.member.data.dto.TeamParticipantUpdateRequest;
import com.api.backend.member.service.MemberService;
import com.api.backend.team.data.dto.TeamParticipantsDto;
import com.api.backend.team.service.TeamParticipantsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.security.Principal;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;


@Api(tags = "회원")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final TeamParticipantsService teamParticipantsService;
    private final AuthService authService;

    private final long COOKIE_EXPIRATION = 7776000;

    @PostMapping("/sign-up")
    public SignUpResponse signUp(
            @RequestBody SignUpRequest request){
        return this.memberService.register(request);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> signIn(@RequestBody SignInRequest signInRequest) {

        SignInResponse signInResponse = memberService.login(signInRequest);

        HttpCookie httpCookie = ResponseCookie.from("refresh-token", signInResponse.getRefreshToken())
                .maxAge(COOKIE_EXPIRATION)
                .httpOnly(true)
                .secure(true)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, httpCookie.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + signInResponse.getAccessToken())
            .body(signInResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logOut(@RequestHeader("Authorization") String requestAccessToken) {

        LogoutResponse logoutResponse = memberService.logout(requestAccessToken);
        ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "")
                .maxAge(0)
                .path("/")
                .build();


        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(logoutResponse);
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestHeader("Authorization") String requestAccessToken) {
        if (!authService.validate(requestAccessToken)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@CookieValue(name = "refresh-token") String requestRefreshToken,
                                     @RequestHeader("Authorization") String requestAccessToken) {
        TokenDto reissuedTokenDto = authService.reissue(requestAccessToken, requestRefreshToken);

        if (reissuedTokenDto != null) {
            ResponseCookie responseCookie = ResponseCookie.from("refresh-token", reissuedTokenDto.getRefreshToken())
                    .maxAge(COOKIE_EXPIRATION)
                    .httpOnly(true)
                    .secure(true)
                    .build();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    // AT 저장
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + reissuedTokenDto.getAccessToken())
                    .build();

        } else {
            ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "")
                    .maxAge(0)
                    .path("/")
                    .build();
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    .build();
        }
    }


    @ApiOperation(value = "내가 속한 팀 참가자 조회 API",notes = "내가 속한 팀 참가자의 정보들을 반환")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "팀 참가자 정보들을 반환")
    })
    @GetMapping("/member/participants")
    public ResponseEntity<Page<TeamParticipantsDto>> getTeamParticipantRequest(
        @ApiIgnore Principal principal,
        Pageable pageable
    ) {
        return ResponseEntity.ok(
            TeamParticipantsDto.fromDtos(
                teamParticipantsService
                    .getTeamParticipantsByUserId(principal, pageable)
            )
        );
    }
    @ApiOperation(value = "팀 참가자 수정 API",notes = "참가자 이미지, 닉네임을 할 수 있다.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "수정된 팀 참가자 정보를 반환"),
        @ApiResponse(code = 200, message = "팀원이 아닌 경우, 허용되지 않은 회원,팀이 해체된 경우"),
    })
    @PostMapping(value = "/member/participant",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TeamParticipantsDto> updateTeamParticipantContentRequest(
        @Valid TeamParticipantUpdateRequest teamParticipantUpdateRequest,
        @ApiIgnore Principal principal
    ) {
        return ResponseEntity.ok(
            TeamParticipantsDto.from(
                teamParticipantsService.updateParticipantContent(teamParticipantUpdateRequest, principal.getName())
            )
        );
    }
}
