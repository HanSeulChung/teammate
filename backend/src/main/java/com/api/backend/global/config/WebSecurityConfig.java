package com.api.backend.global.config;

import com.api.backend.global.oauth2.cookie.HttpCookieOauth2AuthorizationRequestRepository;
import com.api.backend.global.oauth2.handler.OAuth2LoginFailureHandler;
import com.api.backend.global.oauth2.handler.OAuth2LoginSuccessHandler;
import com.api.backend.global.oauth2.service.CustomOAuth2UserService;
import com.api.backend.global.security.jwt.JwtAccessDeniedHandler;
import com.api.backend.global.security.jwt.JwtAuthenticationEntryPoint;
import com.api.backend.global.security.jwt.JwtAuthenticationFilter;
import com.api.backend.global.security.jwt.JwtTokenProvider;
import com.api.backend.global.security.jwt.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Value("${frontend.host}")
    String host;

    @Value("${frontend.port}")
    String port;

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final AuthService authService;

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**",
            "/menus/**",
            "/h2-console/**",
            "/sign-in", "/sign-up", "/logout", "/email-verify/**", "/sign-up/email-check/**", "/oauth2/authorization/**", "/login/oauth2/code/**",
            "/ws"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        StringBuilder url = new StringBuilder();
        url.append("http://").append(host).append(":").append(port);
        http
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .formLogin().disable()
                .logout().disable()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) //customEntryPoint
                .accessDeniedHandler(jwtAccessDeniedHandler) // cutomAccessDeniedHandler

                .and()
                .authorizeRequests() // 요청에 대한 권한 설정
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers("/my-page", "/member/password").authenticated()
                .anyRequest().authenticated()
                .and()
                //oAuth2관련
                .oauth2Login()
                .authorizationEndpoint()
                .authorizationRequestRepository(cookieOauth2AuthorizationRequestRepository())
                .and()
                .userInfoEndpoint().userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2LoginSuccessHandler()) // 동의하고 계속하기를 눌렀을 때 Handler 설정
                .failureHandler(oAuth2LoginFailureHandler); // 소셜 로그인 실패 시 핸들러 설정

        http.cors();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler() {
        return new OAuth2LoginSuccessHandler(jwtTokenProvider, authService,
                cookieOauth2AuthorizationRequestRepository());
    }

    @Bean
    public HttpCookieOauth2AuthorizationRequestRepository cookieOauth2AuthorizationRequestRepository() {
        return new HttpCookieOauth2AuthorizationRequestRepository();
    }

}
