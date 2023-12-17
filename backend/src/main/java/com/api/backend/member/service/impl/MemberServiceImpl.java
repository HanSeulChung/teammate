package com.api.backend.member.service.impl;

import com.api.backend.global.email.MailService;
import com.api.backend.global.exception.CustomException;
import com.api.backend.global.redis.RedisService;
import com.api.backend.global.security.AuthService;
import com.api.backend.global.security.data.dto.TokenDto;
import com.api.backend.global.security.jwt.JwtTokenProvider;
import com.api.backend.member.data.dto.*;
import com.api.backend.member.data.entity.Member;
import com.api.backend.member.data.repository.MemberRepository;
import com.api.backend.member.data.type.Authority;
import com.api.backend.member.data.type.LoginType;
import com.api.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.api.backend.global.exception.type.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final AuthService authService;
    private final MailService mailService;

    @Override
    public SignUpResponse register(SignUpRequest request) {

        boolean exists = this.memberRepository.existsByEmail(request.getEmail());
        if (exists) {
            throw new CustomException(EMAIL_ALREADY_EXIST_EXCEPTION);
        }

        if (!request.getPassword().equals(request.getRepassword())) {
            throw new CustomException(PASSWORD_NOT_MATCH_EXCEPTION);
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));
        Member member = Member.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .name(request.getName())
                .sexType(request.getSexType())
                .loginType(LoginType.TEAMMATE)
                .authority(Authority.USER)
                .isAuthenticatedEmail(false)
                .build();

        memberRepository.save(member);

        sendVerificationMail(member.getEmail());

        return SignUpResponse.builder()
                .email(member.getEmail())
                .message("이메일 인증후 로그인 가능합니다.")
                .build();
    }


    private void sendVerificationMail(String email) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(EMAIL_NOT_FOUND_EXCEPTION));

        UUID uuid = UUID.randomUUID();
        String text = "가입을 축하합니다. 아래 링크를 클릭하여서 가입을 완료하세요.<br>"
                + "<a href='http://localhost:8080/email-verify/" + uuid + "/" + email + "'> 이메일 인증 </a>";

        redisService.setValues(uuid.toString(), member.getEmail(), 60 * 30L, TimeUnit.MINUTES);
        mailService.sendEmail(member.getEmail(), "[teamMate] 회원가입 인증 이메일입니다.", text);

    }

    @Override
    @Transactional
    public boolean verifyEmail(String key, String email) {
        boolean result = false;

        String redisEmail = redisService.getValues(key);
        if (email == null || !email.equals(redisEmail)) {
            sendVerificationMail(email);
            return result;
        }

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(EMAIL_NOT_FOUND_EXCEPTION));

        member.setIsAuthenticatedEmail(true);

        redisService.deleteValues(key);
        result = true;

        return result;
    }


    @Override
    public SignInResponse login(SignInRequest signInRequest) {
        Member member = memberRepository.findByEmail(signInRequest.getEmail())
                .orElseThrow(() -> new CustomException(EMAIL_NOT_FOUND_EXCEPTION));

        if (!member.getIsAuthenticatedEmail()) {
            sendVerificationMail(member.getEmail());
            throw new CustomException(EMAIL_NOT_VERIFICATION_EXCEPTION);
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(member.getMemberId().toString(), signInRequest.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 비밀번호 틀렸을때 BadCredentialsException 던짐 이부분 처리하는 로직 구현해야함

        TokenDto tokenDto = authService.generateToken(authentication.getName(), authService.getAuthorities(authentication));

        return SignInResponse.builder()
                .grantType("Bearer")
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .build();
    }

    public LogoutResponse logout(String requestAccessTokenInHeader) {
        String requestAccessToken = authService.resolveToken(requestAccessTokenInHeader);
        String principal = authService.getPrincipal(requestAccessToken);

        String refreshTokenInRedis = redisService.getValues("RT:" + principal);
        if (refreshTokenInRedis != null) {
            redisService.deleteValues("RT:" + principal);
        }

        long expiration = jwtTokenProvider.getTokenExpirationTime(requestAccessToken) - new Date().getTime();
        redisService.setValues(requestAccessToken,
                "logout",
                expiration,
                TimeUnit.MILLISECONDS);

        return LogoutResponse.builder()
                .message("로그아웃되었습니다.")
                .build();
    }

    @Transactional(readOnly = true)
    public Map<String, String> validateHandling(BindingResult bindingResult) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : bindingResult.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }

        return validatorResult;
    }

    @Override
    public void checkEamilDuplicate(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new CustomException(EMAIL_ALREADY_EXIST_EXCEPTION);
        }
    }

}
