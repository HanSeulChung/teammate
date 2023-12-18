package com.api.backend.member.data.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SignInRequest {
    @Email
    @NotBlank(message = "이메일은 필수 항목 입니다.")
    private String email;
    @NotBlank(message = "비밀번호는 필수 항목 입니다.")
    private String password;

}
