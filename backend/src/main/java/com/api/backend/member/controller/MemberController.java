package com.api.backend.member.controller;

import com.api.backend.member.data.dto.SignInRequest;
import com.api.backend.member.data.dto.SignInResponse;
import com.api.backend.member.data.dto.SignUpRequest;
import com.api.backend.member.data.dto.SignUpResponse;
import com.api.backend.member.service.MemberService;
import com.api.backend.team.data.dto.TeamParticipantsDto;
import com.api.backend.team.service.TeamParticipantsService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
