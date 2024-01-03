package com.api.backend.global.oauth2.handler;

import com.api.backend.global.oauth2.CustomOAuth2User;
import com.api.backend.global.oauth2.cookie.CookieUtils;
import com.api.backend.global.oauth2.cookie.HttpCookieOauth2AuthorizationRequestRepository;
import com.api.backend.global.security.data.dto.TokenDto;
import com.api.backend.global.security.jwt.JwtTokenProvider;
import com.api.backend.global.security.jwt.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.api.backend.global.oauth2.cookie.HttpCookieOauth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.ACCESS_TOKEN;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;
    private final HttpCookieOauth2AuthorizationRequestRepository cookieOauth2AuthorizationRequestRepository;
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            String targetUrl = determineTargetUrl(request, response, authentication);

            TokenDto token = jwtTokenProvider.createToken(oAuth2User.getMemberId().toString(), authService.getAuthorities(authentication));

            authService.saveRefreshToken(oAuth2User.getMemberId().toString(), token.getRefreshToken());

            getRedirectStrategy().sendRedirect(request, response, getRedirectUrl(targetUrl,token));

        } catch (Exception e) {
            throw e;
        }

    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME).map(Cookie::getValue);
        clearAuthenticationAttributes(request, response);
        return redirectUri.orElse("https://teammate-omega.vercel.app/social-success/");
    }

    private String getRedirectUrl(String targetUrl, TokenDto token) {
        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam(ACCESS_TOKEN, token.getAccessToken())
                .queryParam(REFRESH_TOKEN, token.getRefreshToken())
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request,
                                                 HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        cookieOauth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}