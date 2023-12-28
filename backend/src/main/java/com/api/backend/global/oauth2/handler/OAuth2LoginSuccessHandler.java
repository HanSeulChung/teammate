package com.api.backend.global.oauth2.handler;

import com.api.backend.global.exception.CustomException;
import com.api.backend.global.oauth2.CustomOAuth2User;
import com.api.backend.global.oauth2.cookie.CookieUtils;
import com.api.backend.global.oauth2.cookie.HttpCookieOauth2AuthorizationRequestRepository;
import com.api.backend.global.security.data.dto.TokenDto;
import com.api.backend.global.security.jwt.JwtTokenProvider;
import com.api.backend.global.security.jwt.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static com.api.backend.global.exception.type.ErrorCode.TARGET_URL_EMPTY_EXCEPTION;
import static com.api.backend.global.oauth2.cookie.HttpCookieOauth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;
    private final HttpCookieOauth2AuthorizationRequestRepository cookieOauth2AuthorizationRequestRepository;
    private final long COOKIE_EXPIRATION = 7776000;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            String result = setUrlAndToken(
                    getTargetUrl(request), response, oAuth2User
            );
            clearAuthenticationAttributes(request, response);

            TokenDto token = jwtTokenProvider.createToken(oAuth2User.getMemberId().toString(), authService.getAuthorities(authentication));

            String accessToken = token.getAccessToken();
            String refreshToken = token.getRefreshToken();

            authService.saveRefreshToken(oAuth2User.getMemberId().toString(), token.getRefreshToken());

            response.setHeader("authorization", accessToken);
            HttpCookie httpCookie = ResponseCookie.from("refresh-token", refreshToken)
                    .maxAge(COOKIE_EXPIRATION)
                    .httpOnly(true)
                    .secure(true)
                    .build();
            response.setHeader("SET_COOKIE", httpCookie.toString());

            getRedirectStrategy().sendRedirect(request, response, result);

        } catch (Exception e) {
            throw e;
        }

    }

    private String setUrlAndToken(String targetUrl, HttpServletResponse response,
                                  CustomOAuth2User oAuth2User) {

        return UriComponentsBuilder.fromUriString(targetUrl)
                .build().toUriString();
    }

    private String getTargetUrl(HttpServletRequest request) {
        String targetUrl = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue).orElseGet(null);

        if (Objects.isNull(targetUrl)) {
            throw new CustomException(TARGET_URL_EMPTY_EXCEPTION);
        }

        return targetUrl;
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request,
                                                 HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        cookieOauth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}