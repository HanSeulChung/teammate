package com.api.backend.member.data.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMemberPasswordRequest {
    private String oldPassword;
    private String newPassword;
    private String reNewPassword;
}
