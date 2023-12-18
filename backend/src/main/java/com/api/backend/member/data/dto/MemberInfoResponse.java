package com.api.backend.member.data.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberInfoResponse {
    private String email;
    private String name;
}
