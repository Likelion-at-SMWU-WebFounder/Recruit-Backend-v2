package com.smlikelion.webfounder.Recruit.Dto.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smlikelion.webfounder.Recruit.Entity.Joiner;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.util.Map;

@Getter
@Setter
public class RecruitmentRequest {
    @NotNull
    @Valid
    private StudentInfoRequest studentInfo;

    // 답변
    @NotNull
    @Valid
    @JsonProperty("answerList")
    private AnswerListRequest answerList;


    //인터뷰 타임
    @Valid
    @NotNull
    private Map<String, String> interview_time;



    public Joiner toJoiner() {
        Joiner joiner = studentInfo.toJoiner();
        joiner.setInterviewTime(interview_time);
        return joiner;
    }


    public AnswerListRequest getAnswerListRequest() {
        return answerList;
    }
}


