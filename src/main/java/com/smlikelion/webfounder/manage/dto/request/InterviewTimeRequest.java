package com.smlikelion.webfounder.manage.dto.request;

import javax.validation.constraints.NotNull;

public class InterviewTimeRequest {
    @NotNull(message = "해당 지원자를 선택해주세요.")
    private Long joinerId;  // Joiner ID
    @NotNull(message = "면접 날짜를 입력해주세요.")

    private String interviewDate;  // 면접 날짜
    @NotNull(message = "면접 시간을 입력해주세요.")

    private String interviewTime; //면접 시간

    public Long getJoinerId() {
        return joinerId;
    }

    public String getInterviewDate() {
        return interviewDate;
    }

    public String getInterviewTime() {
        return interviewTime;
    }
}
