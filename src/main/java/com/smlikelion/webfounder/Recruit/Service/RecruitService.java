package com.smlikelion.webfounder.Recruit.Service;
import com.smlikelion.webfounder.Recruit.Dto.Request.MailRequestDto;
import com.smlikelion.webfounder.Recruit.Dto.Request.RecruitmentRequest;
import com.smlikelion.webfounder.Recruit.Dto.Response.MailResponseDto;
import com.smlikelion.webfounder.Recruit.Dto.Response.RecruitmentResponse;
import com.smlikelion.webfounder.Recruit.Dto.Response.StudentInfoResponse;
import com.smlikelion.webfounder.Recruit.Entity.*;
import com.smlikelion.webfounder.Recruit.Repository.JoinerRepository;
import com.smlikelion.webfounder.Recruit.Repository.MailRepository;
import com.smlikelion.webfounder.Recruit.exception.DuplicateStudentIdException;
import com.smlikelion.webfounder.Recruit.exception.NotFoundEmailException;
import com.smlikelion.webfounder.manage.entity.Candidate;
import com.smlikelion.webfounder.manage.repository.CandidateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class RecruitService {

    private final JoinerRepository joinerRepository;
    private final CandidateRepository candidateRepository;
    private final MailService mailService;

    public RecruitmentResponse registerRecruitment(RecruitmentRequest request) {

        String studentId = request.getStudentInfo().getStudentId();

        // 동일한 학번을 가진 지원자가 이미 존재하는지 확인
        if (joinerRepository.existsByStudentId(studentId)) {
            throw new DuplicateStudentIdException("동일한 학번으로 중복된 지원서가 이미 제출되었습니다.");
        }

        Joiner joiner = request.getStudentInfo().toJoiner();
        joiner.setInterviewTime(request.getInterview_time());

        List<String> answerList = request.getAnswerListRequest().toAnswerList();
        joiner.setAnswerList(answerList);

        joiner = joinerRepository.save(joiner);
        if(joiner != null) {
            mailService.sendApplyStatusMail(joiner.getEmail());
        }
        StudentInfoResponse studentInfoResponse = joiner.toStudentInfoResponse();

        // cadidate entity 생성 시 서류합 란을 reject로 초기 설정
        Candidate candidate=new Candidate(joiner,"REJECT","REJECT");
        candidateRepository.save(candidate);

        Set<String> interviewTime = request.getInterview_time().values().stream().collect(Collectors.toSet());

        return RecruitmentResponse.builder()
                .id(joiner.getId())
                .studentInfo(studentInfoResponse)
                .answerList(joiner.toAnswerListResponse())
                .interviewTime(interviewTime)
                .build();
    }
}
