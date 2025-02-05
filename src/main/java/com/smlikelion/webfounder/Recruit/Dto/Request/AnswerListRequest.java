package com.smlikelion.webfounder.Recruit.Dto.Request;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class AnswerListRequest {


    @NotBlank(message = "답변을 입력해주세요.")
    // max length
    private String A1;
    @NotBlank(message = "답변을 입력해주세요.")
    private String A2;
    @NotBlank(message = "답변을 입력해주세요.")
    private String A3;
    @NotBlank(message = "답변을 입력해주세요.")
    private String A4;
    @NotBlank(message = "답변을 입력해주세요.")
    private String A5;
    @NotBlank(message = "답변을 입력해주세요.")
    private String A6;
    @NotBlank(message = "답변을 입력해주세요.")
    private String A7;

    public List<String> toAnswerList() {
        return Arrays.asList(A1, A2, A3, A4, A5, A6, A7);
    }

    // ✅ 추가: Map<String, String> 형태로 변환
    public Map<String, String> toAnswerListMap() {
        Map<String, String> answerMap = new LinkedHashMap<>();
        answerMap.put("문항 1", A1);
        answerMap.put("문항 2", A2);
        answerMap.put("문항 3", A3);
        answerMap.put("문항 4", A4);
        answerMap.put("문항 5", A5);
        answerMap.put("문항 6", A6);
        answerMap.put("문항 7", A7);
        return answerMap;
    }
}
