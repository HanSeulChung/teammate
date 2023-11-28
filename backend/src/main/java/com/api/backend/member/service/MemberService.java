package com.api.backend.member.service;

import com.api.backend.member.data.dto.SignInRequest;
import com.api.backend.member.data.dto.SignInResponse;
import com.api.backend.member.data.dto.SignUpRequest;
import com.api.backend.member.data.dto.SignUpResponse;
import org.springframework.stereotype.Service;


public interface MemberService {
    SignUpResponse register(SignUpRequest request);

    SignInResponse login(SignInRequest request);
}
