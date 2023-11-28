package com.api.backend.member.data.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpResponse {

    private String email;
    private String message;

    public static SignUpResponse fromEntity(SignUpRequest signUpRequest){
        return SignUpResponse.builder()
                .email(signUpRequest.getEmail())
                .message("이메일 인증 후 로그인이 가능합니다.")
                .build();
    }
}
