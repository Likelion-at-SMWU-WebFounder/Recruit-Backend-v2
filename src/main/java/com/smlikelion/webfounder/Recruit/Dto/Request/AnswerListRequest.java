package com.smlikelion.webfounder.Recruit.Dto.Request;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.List;

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



}
