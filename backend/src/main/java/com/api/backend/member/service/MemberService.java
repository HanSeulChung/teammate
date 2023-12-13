package com.api.backend.member.service;

import com.api.backend.member.data.dto.*;
import com.api.backend.member.data.entity.Member;
import org.springframework.validation.BindingResult;

import java.util.Map;


public interface MemberService {

    SignUpResponse register(SignUpRequest request);
    boolean verifyEmail(String key, String email);
    SignInResponse login(SignInRequest signInRequest);

    LogoutResponse logout(String requestAccessToken);

    Map<String, String> validateHandling(BindingResult bindingResult);

}
