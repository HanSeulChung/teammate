package com.api.backend.global.oauth2.handler;

import com.api.backend.global.oauth2.CustomOAuth2User;
import com.api.backend.global.security.AuthService;
import com.api.backend.global.security.data.dto.TokenDto;
import com.api.backend.global.security.jwt.JwtTokenProvider;
import com.api.backend.member.data.type.Authority;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            loginSuccess(response, oAuth2User, authentication);
        } catch (Exception e) {
            throw e;
        }

    }

    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User, Authentication authentication) throws IOException {
        TokenDto token = jwtTokenProvider.createToken(oAuth2User.getMemberId().toString(), authService.getAuthorities(authentication));
        response.addHeader("Authorization", "Bearer " + token.getAccessToken());
        response.addCookie(new Cookie("refresh-token", token.getRefreshToken()));

        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Authorization", "");
        authService.sendAccessAndRefreshToken(response, token.getAccessToken(), token.getRefreshToken());
        authService.saveRefreshToken(oAuth2User.getMemberId().toString(), token.getRefreshToken());
    }
}