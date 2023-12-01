package com.api.backend.member.service;

import com.api.backend.member.data.dto.SignUpRequest;
import com.api.backend.member.data.dto.SignUpResponse;
import com.api.backend.member.data.entity.Member;
import org.springframework.stereotype.Service;


public interface MemberService {
    SignUpResponse register(SignUpRequest request);

}
