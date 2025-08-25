package com.smlikelion.webfounder.manage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class ApplicationDocumentPreview {
    private Long joinerId;
    private String name;
    private String phoneNum;
    private String studentID;
    private String track;
    private LocalDateTime submissionTime;
}
