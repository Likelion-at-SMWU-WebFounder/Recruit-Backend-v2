package com.smlikelion.webfounder.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SignInResponse {
    private Long id;
    private String accountId;
    private String role;
    private String accessToken;
    private String refreshToken;
}
