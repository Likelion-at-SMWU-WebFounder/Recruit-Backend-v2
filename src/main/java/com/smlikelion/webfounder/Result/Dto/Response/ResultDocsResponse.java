package com.smlikelion.webfounder.Result.Dto.Response;

import com.smlikelion.webfounder.manage.entity.Docs;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultDocsResponse {

    private String name;
    private Docs docs;
    private String interviewTime;

    public static ResultResponseBuilder builder() {
        return new ResultResponseBuilder();
    }

    public static class ResultResponseBuilder {
        private String name;
        private Docs docs;
        private String interviewTime;

        ResultResponseBuilder() {}

        public ResultResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ResultResponseBuilder docs(Docs docs) {
            this.docs = docs;
            return this;
        }

        public ResultResponseBuilder interviewTime(String interviewTime) {
            this.interviewTime = interviewTime;
            return this;
        }

        public ResultDocsResponse build() {
            return new ResultDocsResponse(name, docs, interviewTime);
        }
    }
}
