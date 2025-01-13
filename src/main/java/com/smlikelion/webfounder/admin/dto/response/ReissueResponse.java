package com.smlikelion.webfounder.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ReissueResponse {
    private String accessToken;
    private String refreshToken;
}
