package com.api.backend.member.controller;

import com.api.backend.member.data.dto.SignUpRequest;
import com.api.backend.member.data.dto.SignUpResponse;
import com.api.backend.member.data.type.SexType;
import com.api.backend.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class MemberControllerTest {
    @Mock
    private MemberService memberService;
    @InjectMocks
    private MemberController memberController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void 회원가입성공() {
        // Given
        SignUpRequest signUpRequset = new SignUpRequest();
        signUpRequset.setEmail("testemail@gmail.com");
        signUpRequset.setPassword("testpassword");
        signUpRequset.setRepassword("testpassword");
        signUpRequset.setName("testname");
        signUpRequset.setNickName("testnickname");
        signUpRequset.setSexType(SexType.FEMALE);

        SignUpResponse signUpResponse = new SignUpResponse();
        signUpResponse.setEmail("testemail@gmail.com");
        signUpResponse.setMessage("이메일 인증후 로그인이 가능합니다.");

        when(memberService.register(signUpRequset)).thenReturn(signUpResponse);

        // When
        ResponseEntity<?> responseEntity = memberController.signUp(signUpRequset);

        SignUpResponse response = (SignUpResponse) responseEntity.getBody();
        // Then
        assertNotNull(responseEntity.getBody());
        assertEquals("testemail@gmail.com", response.getEmail());
        assertEquals("이메일 인증후 로그인이 가능합니다.",response.getMessage());
    }


}