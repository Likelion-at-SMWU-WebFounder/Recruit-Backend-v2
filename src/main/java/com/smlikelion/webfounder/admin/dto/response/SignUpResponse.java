package com.smlikelion.webfounder.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SignUpResponse {
    private Long id;
    private String accountId;
    private String name;
}
