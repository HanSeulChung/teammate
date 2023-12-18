package com.api.backend.member.data.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UpdateMemberPasswordRequest {
    private String oldPassword;
    @NotNull(message = "새 비밀번호 입력은 필수 입니다.")
    @Size(min = 8,message = "비밀번호는 최소 8글자입니다.")
    private String newPassword;
    @NotNull(message = "새 비밀번호 확인은 필수 입니다.")
    @Size(min = 8,message = "비밀번호는 최소 8글자입니다.")
    private String reNewPassword;
}
