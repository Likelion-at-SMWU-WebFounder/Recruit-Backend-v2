package com.smlikelion.webfounder.Recruit.Dto.Response;

import com.smlikelion.webfounder.Recruit.Dto.Request.AnswerListRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AnswerListResponse {

    private String A1;
    private String A2;
    private String A3;
    private String A4;
    private String A5;
    private String A6;
    private String A7;

    // ✅ AnswerListRequest를 AnswerListResponse로 변환하는 메서드 추가
    public static AnswerListResponse fromRequest(AnswerListRequest request) {
        return AnswerListResponse.builder()
                .A1(request.getA1())
                .A2(request.getA2())
                .A3(request.getA3())
                .A4(request.getA4())
                .A5(request.getA5())
                .A6(request.getA6())
                .A7(request.getA7())
                .build();
    }
}