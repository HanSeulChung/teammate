package com.api.backend.member.controller;

import com.api.backend.member.data.dto.SignUpRequest;
import com.api.backend.member.data.dto.SignUpResponse;
import com.api.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    @PostMapping("/sign-up")
    public SignUpResponse signUp (
            @RequestBody SignUpRequest request
    )throws Exception {
        return this.memberService.register(request);
    }
}
