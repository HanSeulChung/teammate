package com.api.backend.member.controller;

import com.api.backend.member.data.dto.SignInRequest;
import com.api.backend.member.data.dto.SignInResponse;
import com.api.backend.member.data.dto.SignUpRequest;
import com.api.backend.member.data.dto.SignUpResponse;
import com.api.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/sign-in")
    public ResponseEntity<SignInResponse> signIn(@RequestBody SignInRequest signInRequest) {
        SignInResponse signInResponse = memberService.login(signInRequest);

        return ResponseEntity.ok(signInResponse);
    }

    @PostMapping("/sign-up")
    public SignUpResponse signUp(
            @RequestBody SignUpRequest request
    ) throws Exception {
        return this.memberService.register(request);
    }
}
