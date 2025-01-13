package com.smlikelion.webfounder.Recruit.Dto.Request;

import com.smlikelion.webfounder.Recruit.Dto.Response.AnswerListResponse;
import com.smlikelion.webfounder.Recruit.Entity.Joiner;
import com.smlikelion.webfounder.Recruit.Entity.Programmers;
import com.smlikelion.webfounder.Recruit.Entity.SchoolStatus;
import com.smlikelion.webfounder.Recruit.Entity.Track;
import com.smlikelion.webfounder.manage.entity.Candidate;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter

public class RecruitmentRequest {
    @NotNull
    @Valid
    private StudentInfoRequest studentInfo;

    // 답변
    @NotNull
    @Valid
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


