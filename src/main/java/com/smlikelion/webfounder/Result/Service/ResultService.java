package com.smlikelion.webfounder.Result.Service;
import com.smlikelion.webfounder.Recruit.Entity.Joiner;
import com.smlikelion.webfounder.Recruit.Repository.JoinerRepository;
import com.smlikelion.webfounder.Result.Dto.Request.ResultDocsRequest;
import com.smlikelion.webfounder.Result.Dto.Request.ResultInterviewRequest;
import com.smlikelion.webfounder.Result.Dto.Response.ResultDocsResponse;
import com.smlikelion.webfounder.Result.Dto.Response.ResultInterviewResponse;
import com.smlikelion.webfounder.Result.Exception.CandidateNotFoundException;
import com.smlikelion.webfounder.Result.Exception.IncorrectPasswordException;
import com.smlikelion.webfounder.global.dto.response.BaseResponse;
import com.smlikelion.webfounder.manage.entity.Candidate;
import com.smlikelion.webfounder.manage.entity.Docs;
import com.smlikelion.webfounder.manage.entity.Interview;
import com.smlikelion.webfounder.manage.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResultService {

    private final JoinerRepository joinerRepository;
    private final CandidateRepository candidateRepository;

    public BaseResponse<ResultDocsResponse> getDocsResult(ResultDocsRequest request) {
        Joiner joiner = getJoiner(request);

        Candidate candidate = candidateRepository.findByJoinerAndDocs(joiner, Docs.PASS);

        if (candidate == null) {
            //불합격자
            ResultDocsResponse resultResponse = ResultDocsResponse.builder()
                    .name(joiner.getName())
                    .docs(Docs.REJECT)
                    .build();
            return new BaseResponse<>(HttpStatus.OK.value(), "요청에 성공했습니다.", resultResponse);
        }
        ResultDocsResponse resultResponse = ResultDocsResponse.builder()
                .name(joiner.getName())
                .docs(candidate.getDocs())
                .interviewTime(candidate.getInterviewTime())
                .build();

        return new BaseResponse<>(HttpStatus.OK.value(), "요청에 성공했습니다.", resultResponse);
    }

    public BaseResponse<ResultInterviewResponse> getInterviewResult(ResultInterviewRequest request) {
        Joiner joiner = getJoiner(request);

        Candidate candidate = candidateRepository.findByJoinerAndInterview(joiner, Interview.PASS);
        if (candidate == null) {
            //불합격자
            ResultInterviewResponse resultResponse = ResultInterviewResponse.builder()
                    .name(joiner.getName())
                    .interview(Interview.REJECT)
                    .track(joiner.getTrack())
                    .build();
            return new BaseResponse<>(HttpStatus.OK.value(), "요청에 성공했습니다.", resultResponse);
        }
        // 합격자의 경우
        ResultInterviewResponse resultResponse = ResultInterviewResponse.builder()
                .name(joiner.getName())
                .interview(candidate.getInterview())
                .track(joiner.getTrack())
                .build();

        return new BaseResponse<>(HttpStatus.OK.value(), "요청에 성공했습니다.", resultResponse);
    }

    private Joiner getJoiner(ResultDocsRequest request) {
        if (request.getName() == null || request.getStudentId() == null || request.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다. 입력을 확인해주세요.");
        }

        Joiner joiner = joinerRepository.findByNameAndStudentId(request.getName(), request.getStudentId());

        if (joiner == null) {
            throw new CandidateNotFoundException("존재하지 않는 회원 정보입니다.");
        }

        if (!joiner.getPassword().equals(request.getPassword())) {
            BaseResponse<Void> errorResponse = new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), "비밀번호가 일치하지 않습니다.", null);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.", null);
        }

        return joiner;
    }

    private Joiner getJoiner(ResultInterviewRequest request) {
        if (request.getName() == null || request.getStudentId() == null || request.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다. 입력을 확인해주세요.");
        }

        Joiner joiner = joinerRepository.findByNameAndStudentId(request.getName(), request.getStudentId());

        if (joiner == null) {
            throw new CandidateNotFoundException("존재하지 않는 회원 정보입니다.");
        }

        if (!joiner.getPassword().equals(request.getPassword())) {
            BaseResponse<Void> errorResponse = new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), "비밀번호가 일치하지 않습니다.", null);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.", null);
        }

        return joiner;
    }


}
