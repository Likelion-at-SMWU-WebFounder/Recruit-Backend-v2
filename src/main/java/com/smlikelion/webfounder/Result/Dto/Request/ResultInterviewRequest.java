package com.smlikelion.webfounder.Result.Dto.Request;

import lombok.Getter;

import javax.validation.constraints.NotNull;
@Getter
public class ResultInterviewRequest {
    @NotNull(message = "이름을 입력을 해주세요.")
    private String name;
    @NotNull(message = "학번을 입력을 해주세요")
    private String studentId;
    @NotNull(message = "비밀번호를 입력해주세요.")
    private String password;
}
