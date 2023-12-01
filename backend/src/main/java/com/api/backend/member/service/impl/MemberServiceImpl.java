package com.api.backend.member.service.impl;

import com.api.backend.global.exception.CustomException;

import com.api.backend.member.data.dto.SignUpRequest;
import com.api.backend.member.data.dto.SignUpResponse;
import com.api.backend.member.data.entity.Member;
import com.api.backend.member.data.repository.MemberRepository;
import com.api.backend.member.data.type.Authority;
import com.api.backend.member.data.type.LoginType;
import com.api.backend.member.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.api.backend.global.exception.type.ErrorCode.*;

@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


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
                .nickName(request.getNickName())
                .sexType(request.getSexType())
                .loginType(LoginType.TEAMMATE)
                .authority(Authority.USER)
                .build();

        memberRepository.save(member);


        return SignUpResponse.builder()
                .email(member.getEmail())
                .message("이메일 인증후 로그인 가능합니다.")
                .build();
    }
}
