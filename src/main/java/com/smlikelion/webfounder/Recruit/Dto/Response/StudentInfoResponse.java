package com.smlikelion.webfounder.Recruit.Dto.Response;

import com.smlikelion.webfounder.Recruit.Entity.Programmers;
import com.smlikelion.webfounder.manage.entity.Candidate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@AllArgsConstructor
@Builder
public class StudentInfoResponse {
    private String name;
    private String password;
    private String portfolio;
    private String track;
    private String phoneNumber;
    private String email;
    private long studentId;
    private String major;

    private int completedSem;
    private String schoolStatus;
    private String graduatedYear;
    private String programmers;
    private String programmersImg;
    private Candidate candidate;

    private boolean agreeToTerms;
    private boolean agreeToEventParticipation;



}
