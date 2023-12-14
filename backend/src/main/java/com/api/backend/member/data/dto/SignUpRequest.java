package com.api.backend.member.data.dto;


import com.api.backend.member.data.type.SexType;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @Email
    @NotBlank(message = "이메일은 필수 항목 입니다.")
    private String email;
    @NotBlank(message = "비밀번호는 필수 항목 입니다.")
    private String password;
    @NotBlank(message = "비밀번호 확인은 필수 항목 입니다.")
    private String repassword;
    @NotBlank(message = "이름은 필수 항목 입니다.")
    private String name;
    @NotBlank(message = "별명은 필수 항목 입니다.")
    private String nickName;
    @NotNull(message = "올바른 성별을 입력해주세요")
    private SexType sexType;
}
