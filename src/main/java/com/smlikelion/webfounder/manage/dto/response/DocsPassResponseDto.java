// 서류 합격자 전채 조회
package com.smlikelion.webfounder.manage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class DocsPassResponseDto {
    private Long joinerId;
    private String name;
    private String phoneNum;
    private String studentID;
    private String track;
    private String interviewTime;
    private String submissionTime;
}
