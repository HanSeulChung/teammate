package com.api.backend.member.data.dto;

import com.api.backend.member.data.type.LoginType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberInfoResponse {
    private String email;
    private String name;
    private LoginType loginType;
}
