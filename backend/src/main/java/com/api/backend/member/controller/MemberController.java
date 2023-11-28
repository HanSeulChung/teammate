package com.api.backend.member.controller;

import com.api.backend.member.data.dto.SignUpRequest;
import com.api.backend.member.data.dto.SignUpResponse;
import com.api.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(
            @RequestBody @Valid SignUpRequest request
            ){
        SignUpResponse response = this.memberService.register(request);
        return ResponseEntity.ok().body(response);
    }


}
