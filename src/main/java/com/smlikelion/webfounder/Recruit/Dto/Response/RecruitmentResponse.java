package com.smlikelion.webfounder.Recruit.Dto.Response;

import com.smlikelion.webfounder.Recruit.Entity.Programmers;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Set;

@Getter
@AllArgsConstructor
@Builder
public class RecruitmentResponse {
    private Long id;
    private StudentInfoResponse studentInfo;
    private ArrayList<String> answerList;
    private Set<String> interviewTime;

    public static RecruitmentResponseBuilder builder() {
        return new RecruitmentResponseBuilder();
    }

    public static class RecruitmentResponseBuilder {
        private Long id;
        private StudentInfoResponse studentInfo;
        private ArrayList<String> answerList;
        private Set<String> interviewTime;

        public static RecruitmentResponseBuilder builder() {
            return new RecruitmentResponseBuilder();
        }

        public RecruitmentResponseBuilder answerList(AnswerListResponse answerListResponse) {
            this.answerList = new ArrayList<>();

            if (answerListResponse != null) {
                this.answerList.add(answerListResponse.getA1());
                this.answerList.add(answerListResponse.getA2());
                this.answerList.add(answerListResponse.getA3());
                this.answerList.add(answerListResponse.getA4());
                this.answerList.add(answerListResponse.getA5());
                this.answerList.add(answerListResponse.getA6());
                this.answerList.add(answerListResponse.getA7());
            }

            return this;
        }

        public RecruitmentResponseBuilder interviewTime(Set<String> interviewTime) {
            this.interviewTime = interviewTime;
            return this;
        }

        public RecruitmentResponse build() {
            return new RecruitmentResponse(id, studentInfo, answerList, interviewTime);
        }
    }
}
