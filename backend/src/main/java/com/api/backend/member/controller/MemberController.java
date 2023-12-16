package com.api.backend.member.controller;

import com.api.backend.global.security.AuthService;
import com.api.backend.global.security.data.dto.TokenDto;
import com.api.backend.member.data.dto.*;
import com.api.backend.member.service.MemberService;
import com.api.backend.team.data.dto.TeamParticipantsDto;
import com.api.backend.team.service.TeamParticipantsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@Api(tags = "회원")
@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final TeamParticipantsService teamParticipantsService;
    private final AuthService authService;

    private final long COOKIE_EXPIRATION = 7776000;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(
            @Valid @RequestBody SignUpRequest request,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            Map<String, String> validatorResult = memberService.validateHandling(bindingResult);

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(validatorResult);
        }

        return ResponseEntity.ok(this.memberService.register(request));
    }

    @PostMapping("/sign-up/email-check")
    public ResponseEntity<Boolean> checkEmailDuplicate(
            @RequestBody Map<String, String> request) {
        String email = request.get("email");
        memberService.checkEamilDuplicate(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/email-verify/{key}/{email}")
    public ResponseEntity<String> getVerify(@PathVariable("key") String key, @PathVariable("email") String email) {

        boolean result = memberService.verifyEmail(key, email);

        if (result) {
            return ResponseEntity.ok("이메일 인증에 성공했습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("이메일 인증에 실패했습니다. 이메일을 다시 확인해주세요");
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(
            @RequestBody @Valid SignInRequest signInRequest,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            Map<String, String> validatorResult = memberService.validateHandling(bindingResult);

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(validatorResult);
        }

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


    @ApiOperation(value = "내가 속한 팀 참가자 조회 API", notes = "내가 속한 팀 참가자의 정보들을 반환")
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

    @ApiOperation(value = "팀 참가자 수정 API", notes = "참가자 이미지, 닉네임을 할 수 있다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "수정된 팀 참가자 정보를 반환"),
            @ApiResponse(code = 200, message = "팀원이 아닌 경우, 허용되지 않은 회원,팀이 해체된 경우"),
    })
    @PostMapping(value = "/member/participant", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
