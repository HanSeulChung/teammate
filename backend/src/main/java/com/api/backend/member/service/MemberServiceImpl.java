package com.api.backend.member.service;

import com.api.backend.global.exception.CustomException;
import com.api.backend.global.exception.type.ErrorCode;
import com.api.backend.member.data.dto.SignInRequest;
import com.api.backend.member.data.dto.SignInResponse;
import com.api.backend.member.data.dto.SignUpRequest;
import com.api.backend.member.data.dto.SignUpResponse;
import com.api.backend.member.data.entity.Member;
import com.api.backend.member.data.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.api.backend.global.exception.type.ErrorCode.*;

@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SignUpResponse register(SignUpRequest request) {

        boolean exists = this.memberRepository.existByEmail(request.getEmail());
        if(exists){
            throw new CustomException(EMAIL_ALREADY_EXIST_EXCEPTION);
        }

        if(!request.getPassword().equals(request.getRepassword())){
            throw new CustomException(PASSWORD_NOT_MATCH_EXCEPTION);
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));
        Member member = memberRepository.save(request.toEntity());


        return SignUpResponse.fromEntity(request);
    }

    @Override
    public SignInResponse login(SignInRequest request) {
        return null;
    }
}
