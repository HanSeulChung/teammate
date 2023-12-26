package com.api.backend.member.service;

import com.api.backend.member.data.dto.*;
import com.api.backend.member.data.entity.Member;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface MemberService {

    SignUpResponse register(SignUpRequest request);

    boolean verifyEmail(String key, String email);

    SignInResponse login(SignInRequest signInRequest);

    LogoutResponse logout(String requestAccessToken);


    MemberInfoResponse getMemberInfo(String requestAccessTokenInHeader);

    void updateMemberPassword(String accessToken, UpdateMemberPasswordRequest updateMemberPasswordRequest);

    Map<String, String> validateHandling(BindingResult bindingResult);

    void checkEamilDuplicate(String email);

    List<Member> getMembersIsAuthenticatedEmailFalse(Boolean emailAuthenticationYN, LocalDateTime now);

    void deleteMember(Member member);
}
