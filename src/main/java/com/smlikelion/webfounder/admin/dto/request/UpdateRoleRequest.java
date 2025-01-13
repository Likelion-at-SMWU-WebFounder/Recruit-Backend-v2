package com.smlikelion.webfounder.admin.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoleRequest {
    @NotNull(message = "아이디 키을 입력해야 합니다.")
    private Long id;

    @NotNull(message = "아이디 입력을 해야 합니다.")
    private String accountId;

    @NotNull(message = "바꿀 역할를 입력해야 합니다.")
    private String role;
}
