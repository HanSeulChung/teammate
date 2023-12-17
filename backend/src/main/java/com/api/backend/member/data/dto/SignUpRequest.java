package com.api.backend.member.data.dto;


import com.api.backend.member.data.type.SexType;
import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @Email
    @NotBlank(message = "이메일은 필수 항목 입니다.")
    private String email;
    @Size(min = 8, message = "비밀번호는 최소 8글자 입니다.")
    @NotBlank(message = "비밀번호는 필수 항목 입니다.")
    private String password;
    @Size(min = 8, message = "비밀번호 확인은 최소 8글자 입니다.")
    @NotBlank(message = "비밀번호 확인은 필수 항목 입니다.")
    private String repassword;
    @NotBlank(message = "이름은 필수 항목 입니다.")
    private String name;
    @NotNull(message = "성별은 필수 항목 입니다.")
    private SexType sexType;
}
