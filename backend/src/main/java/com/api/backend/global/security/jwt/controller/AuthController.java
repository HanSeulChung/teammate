package com.api.backend.global.security.jwt.controller;

import com.api.backend.global.security.data.dto.TokenDto;
import com.api.backend.global.security.jwt.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@Api(tags = "회원")
@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    private final long COOKIE_EXPIRATION = 7776000;

    @ApiOperation(value = "토큰 검증 API", notes = "토큰 만료 여부에 따라 상태코드를 반환한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "토큰이 유효할때 정상처리"),
            @ApiResponse(code = 401, message = "토큰이 만료되었을때 재발급처리 로직으로 이동해야한다.")

    })
    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestHeader("Authorization") String requestAccessToken) {
        if (!authService.validate(requestAccessToken)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @ApiOperation(value = "토큰 재발급 API", notes = "access 와 refresh 토큰 정보로 새 토큰을 발급받는다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "토큰이 정상적으로 재발급 되었을 때 새 토큰 값을 헤더에 채운다."),
            @ApiResponse(code = 401, message = "토큰 발급에 실패 했을 경우 헤더 값을 지워 준다. 이때 재 로그인 과정을 거쳐야 한다.")
    })
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
}
