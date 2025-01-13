package com.smlikelion.webfounder.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UpdateRoleResponse {
    private Long id;
    private String accountId;
    private String role;
}
