package com.api.backend.member.controller;

import com.api.backend.member.data.dto.*;
import com.api.backend.member.data.dto.TeamParticipantUpdateRequest;
import com.api.backend.member.service.MemberService;
import com.api.backend.team.data.dto.TeamParticipantsDto;
import com.api.backend.team.service.TeamParticipantsService;
import java.security.Principal;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final TeamParticipantsService teamParticipantsService;

    private final long COOKIE_EXPIRATION = 7776000;

    @PostMapping("/sign-up")
    public SignUpResponse signUp(
            @RequestBody SignUpRequest request){
        return this.memberService.register(request);
    }

    @GetMapping("/sign-in")
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
                .build();
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


    @GetMapping("/member/participants")
    public ResponseEntity<Page<TeamParticipantsDto>> getTeamParticipantRequest(
        Principal principal,
        Pageable pageable
    ) {
        return ResponseEntity.ok(
            TeamParticipantsDto.fromDtos(
                teamParticipantsService
                    .getTeamParticipantsByUserId(principal, pageable)
            )
        );
    }

    @PatchMapping("/member/participant")
    public ResponseEntity<TeamParticipantsDto> updateTeamParticipantContentRequest(
        @RequestBody @Valid
        TeamParticipantUpdateRequest teamParticipantUpdateRequest,
        Principal principal
    ) {
        return ResponseEntity.ok(
            TeamParticipantsDto.from(
                teamParticipantsService.updateParticipantContent(teamParticipantUpdateRequest, principal.getName())
            )
        );
    }
}
