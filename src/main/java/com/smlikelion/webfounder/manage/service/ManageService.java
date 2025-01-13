package com.smlikelion.webfounder.manage.service;

import com.smlikelion.webfounder.Recruit.Entity.Joiner;
import com.smlikelion.webfounder.Recruit.Entity.Track;
import com.smlikelion.webfounder.admin.entity.Role;
import com.smlikelion.webfounder.admin.exception.UnauthorizedRoleException;
import com.smlikelion.webfounder.Recruit.Repository.JoinerRepository;
import com.smlikelion.webfounder.manage.dto.request.DocsInterPassRequestDto;
import com.smlikelion.webfounder.manage.dto.request.DocsQuestRequest;
import com.smlikelion.webfounder.manage.dto.request.DocsQuestUpdateRequest;
import com.smlikelion.webfounder.manage.dto.request.InterviewTimeRequest;
import com.smlikelion.webfounder.manage.dto.response.*;
import com.smlikelion.webfounder.manage.entity.Candidate;
import com.smlikelion.webfounder.manage.entity.Docs;
import com.smlikelion.webfounder.manage.entity.Interview;
import com.smlikelion.webfounder.manage.entity.Question;
import com.smlikelion.webfounder.manage.exception.*;
import com.smlikelion.webfounder.manage.repository.CandidateRepository;
import com.smlikelion.webfounder.manage.repository.QuestionRepository;
import com.smlikelion.webfounder.security.AuthInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManageService {

    private final QuestionRepository questionRepository;
    private final CandidateRepository candidateRepository;
    private final JoinerRepository joinerRepository;

    public DocsQuestResponse registerQuestion(AuthInfo authInfo, DocsQuestRequest request) {
        if(!hasValidRoles(authInfo, List.of(Role.SUPERUSER, Role.MANAGER))) {
            throw new UnauthorizedRoleException("접근 권한이 없습니다.");
        }

        validateCurrentYear(request.getYear());
        Track track = validateTrackName(request.getTrack());
        validateQuestionNumber(request, track, null);

        Question question = questionRepository.save(
                Question.builder()
                        .year(request.getYear())
                        .track(track)
                        .number(request.getNumber())
                        .content(request.getContent())
                        .maxLength(request.getMaxLength())
                        .build()
        );

        return mapQuestionToDocsQuestResponse(question);
    }

    public DocsQuestResponse updateQuestion(AuthInfo authInfo, Long id, DocsQuestRequest request) {
        if(!hasValidRoles(authInfo, List.of(Role.SUPERUSER, Role.MANAGER))) {
            throw new UnauthorizedRoleException("접근 권한이 없습니다.");
        }

        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new NotFoundQuestionException("해당 id를 가진 문항이 존재하지 않습니다."));

        validateCurrentYear(request.getYear());
        Track track = validateTrackName(request.getTrack());
        validateCurrentTrack(question.getTrack(), track);
        validateQuestionNumber(request, track, question);

        //문항 수정
        question.setTrack(track);
        question.setNumber(request.getNumber());
        question.setContent(request.getContent());
        question.setMaxLength(request.getMaxLength());

        question = questionRepository.save(question);

        return mapQuestionToDocsQuestResponse(question);
    }

    public List<DocsQuestResponse> updateQuestions(AuthInfo authInfo, List<DocsQuestUpdateRequest> requests) {
        if(!hasValidRoles(authInfo, List.of(Role.SUPERUSER, Role.MANAGER))) {
            throw new UnauthorizedRoleException("접근 권한이 없습니다.");
        }

        List<DocsQuestResponse> docsQuestResponseList = new ArrayList<>();

        for(DocsQuestUpdateRequest request : requests){
            Question question = questionRepository.findById(request.getId())
                    .orElseThrow(() -> new NotFoundQuestionException("해당 id를 가진 문항이 존재하지 않습니다."));

            validateCurrentYear(request.getYear());
            Track track = validateTrackName(request.getTrack());
            validateCurrentTrack(question.getTrack(), track);
            validateQuestionNumberByUpdate(request, track, question);

            //문항 수정
            question.setTrack(track);
            question.setNumber(request.getNumber());
            question.setContent(request.getContent());
            question.setMaxLength(request.getMaxLength());

            question = questionRepository.save(question);
            docsQuestResponseList.add(mapQuestionToDocsQuestResponse(question));
        }

        return docsQuestResponseList;
    }

    public DocsQuestResponse deleteQuestion(AuthInfo authInfo, Long id) {
        if(!hasValidRoles(authInfo, List.of(Role.SUPERUSER, Role.MANAGER))) {
            throw new UnauthorizedRoleException("접근 권한이 없습니다.");
        }

        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new NotFoundQuestionException("해당 id를 가진 문항이 존재하지 않습니다."));

        validateCurrentYear(question.getYear());

        questionRepository.delete(question);

        return mapQuestionToDocsQuestResponse(question);
    }

    public List<DocsQuestResponse> retrieveQuestionByYearAndTrack(AuthInfo authInfo, Long year, String track) {
        if(!hasValidRoles(authInfo, List.of(Role.SUPERUSER, Role.MANAGER))) {
            throw new UnauthorizedRoleException("접근 권한이 없습니다.");
        }

        Track requestedTrack = validateTrackName(track);

        List<Question> questionList = questionRepository.findAllByYearAndTrack(year, requestedTrack);

        if (questionList == null) {
            questionList = Collections.emptyList();
        }
        return questionList.stream()
                .map(this::mapQuestionToDocsQuestResponse)
                .collect(Collectors.toList());
    }

    private DocsQuestResponse mapQuestionToDocsQuestResponse(Question question) {
        return DocsQuestResponse.builder()
                .id(question.getQuestionId())
                .year(question.getYear())
                .track(question.getTrack().getTrackName())
                .number(question.getNumber())
                .content(question.getContent())
                .maxLength(question.getMaxLength())
                .build();
    }

    private boolean hasValidRoles(AuthInfo authInfo, List<Role> allowedRoles) {
        return authInfo.getRoles().stream().anyMatch(allowedRoles::contains);
    }


    private void validateCurrentYear(Long requestedYear) {
        if (Year.now().getValue() != requestedYear) {
            throw new MismatchedYearException("현재 년도와 다른 년도입니다.");
        }
    }

    private Track validateTrackName(String requestedTrack) {
        Track track = Optional.ofNullable(Track.getTrackByName(requestedTrack))
                .orElseThrow(() -> new UnsupportedTrackException("해당 트랙이 지원되지 않습니다."));
        return track;
    }

    private void validateCurrentTrack(Track beforeTrack, Track requestedTrack) {
        if (beforeTrack != requestedTrack) {
            throw new MismatchedTrackException("해당 트랙은 기존의 트랙과 달라서 수정할 수 없습니다.");
        }
    }

    private void validateQuestionNumber(DocsQuestRequest request, Track track, Question existingQuestion) {
        Question foundQuestion = questionRepository.findByYearAndTrackAndNumber(request.getYear(), track, request.getNumber());
        if (foundQuestion != null && existingQuestion != foundQuestion) {
            throw new AlreadyExistsQuestionNumberException("이미 해당 년도 해당 트랙의 번호 문항이 존재합니다.");
        }
    }

    private void validateQuestionNumberByUpdate(DocsQuestUpdateRequest request, Track track, Question existingQuestion) {
        Question foundQuestion = questionRepository.findByYearAndTrackAndNumber(request.getYear(), track, request.getNumber());
        if (foundQuestion != null && existingQuestion != foundQuestion) {
            throw new AlreadyExistsQuestionNumberException("이미 해당 년도 해당 트랙의 번호 문항이 존재합니다.");
        }
    }

    private void validateQuestionList(List<Question> questionList) {
        if (questionList.isEmpty()) {
            throw new NotFoundQuestionException("해당 년도 해당 트랙의 문항이 존재하지 않습니다.");
        }

    }

    public List<Long> docsPass(DocsInterPassRequestDto requestDto){
        List<Long> passedJoinerIds = new ArrayList<>();

        // 요청 데이터가 하나라도 유효하지 않다면 에러 발생
        for (Long joinerId : requestDto.getJoinerIds()) {
            Joiner joiner = joinerRepository.findById(joinerId).orElseThrow(
                    () -> new NotFoundJoinerException(joinerId+"번 joiner는 존재하지 않습니다")
            );
            Candidate candidate = candidateRepository.findByJoiner(joiner).orElseThrow(
                    () -> new NotFoundCandidateException(joinerId+"번 candidate는 존재하지 않습니다")
            );
        }

        for(Long joinerId : requestDto.getJoinerIds()){
            Joiner joiner = joinerRepository.findById(joinerId).orElseThrow(
                    () -> new NotFoundJoinerException(joinerId+"번 joiner는 존재하지 않습니다")
            );
            Candidate candidate = candidateRepository.findByJoiner(joiner).orElseThrow(
                    () -> new NotFoundCandidateException(joinerId+"번 candidate는 존재하지 않습니다")
            );
            candidate.setDocs(Docs.PASS);
            candidateRepository.save(candidate);
            passedJoinerIds.add(joinerId);
        }

        return passedJoinerIds;
    }

    public List<Long> docsFail(DocsInterPassRequestDto requestDto){
        List<Long> failedJoinerIds = new ArrayList<>();

        // 요청 데이터가 하나라도 유효하지 않다면 에러 발생
        for (Long joinerId : requestDto.getJoinerIds()) {
            Joiner joiner = joinerRepository.findById(joinerId).orElseThrow(
                    () -> new NotFoundJoinerException(joinerId+"번 joiner는 존재하지 않습니다")
            );
            Candidate candidate = candidateRepository.findByJoiner(joiner).orElseThrow(
                    () -> new NotFoundCandidateException(joinerId+"번 candidate는 존재하지 않습니다")
            );
        }

        for(Long joinerId : requestDto.getJoinerIds()){
            Joiner joiner = joinerRepository.findById(joinerId).orElseThrow(
                    () -> new NotFoundJoinerException(joinerId+"번 joiner는 존재하지 않습니다")
            );
            Candidate candidate = candidateRepository.findByJoiner(joiner).orElseThrow(
                    () -> new NotFoundCandidateException(joinerId+"번 candidate는 존재하지 않습니다")
            );
            candidate.setDocs(Docs.REJECT);
            candidateRepository.save(candidate);
            failedJoinerIds.add(joinerId);
        }

        return failedJoinerIds;
    }

    public List<Long> interviewPass(DocsInterPassRequestDto requestDto){
        List<Long> passedJoinerIds = new ArrayList<>();

        // 요청 데이터가 하나라도 유효하지 않다면 에러 발생
        for (Long joinerId : requestDto.getJoinerIds()) {
            Joiner joiner = joinerRepository.findById(joinerId).orElseThrow(
                    () -> new NotFoundJoinerException(joinerId+"번 joiner는 존재하지 않습니다")
            );
            Candidate candidate = candidateRepository.findByJoiner(joiner).orElseThrow(
                    () -> new NotFoundCandidateException(joinerId+"번 candidate는 존재하지 않습니다")
            );

            // 요청 데이터에 서류 불합격자가 있다면 에러 발생
            validateInterviewPassJoiner(candidate);
        }

        for (Long joinerId : requestDto.getJoinerIds()) {
            Joiner joiner = joinerRepository.findById(joinerId).orElseThrow(
                    () -> new NotFoundJoinerException(joinerId+"번 joiner는 존재하지 않습니다")
            );
            Candidate candidate = candidateRepository.findByJoiner(joiner).orElseThrow(
                    () -> new NotFoundCandidateException(joinerId+"번 candidate는 존재하지 않습니다")
            );

            candidate.setInterview(Interview.PASS);
            candidateRepository.save(candidate);
            passedJoinerIds.add(joinerId);
        }

        return passedJoinerIds;
    }

    private void validateInterviewPassJoiner(Candidate candidate) {
        if(candidate.getDocs().equals(Docs.REJECT)){
            throw new InvalidInterviewPassException(candidate.getJoiner().getId()+"번 지원자는 서류 불합격자 입니다.");
        }
    }

    public List<Long> interviewFail(DocsInterPassRequestDto requestDto){
        List<Long> failedJoinerIds = new ArrayList<>();

        // 요청 데이터가 하나라도 유효하지 않다면 에러 발생
        for (Long joinerId : requestDto.getJoinerIds()) {
            Joiner joiner = joinerRepository.findById(joinerId).orElseThrow(
                    () -> new NotFoundJoinerException(joinerId+"번 joiner는 존재하지 않습니다")
            );
            Candidate candidate = candidateRepository.findByJoiner(joiner).orElseThrow(
                    () -> new NotFoundCandidateException(joinerId+"번 candidate는 존재하지 않습니다")
            );
        }

        for(Long joinerId : requestDto.getJoinerIds()){
            Joiner joiner = joinerRepository.findById(joinerId).orElseThrow(
                    () -> new NotFoundJoinerException(joinerId+"번 joiner는 존재하지 않습니다")
            );
            Candidate candidate = candidateRepository.findByJoiner(joiner).orElseThrow(
                    () -> new NotFoundCandidateException(joinerId+"번 candidate는 존재하지 않습니다")
            );
            candidate.setInterview(Interview.REJECT);
            candidateRepository.save(candidate);
            failedJoinerIds.add(joinerId);
        }

        return failedJoinerIds;
    }

    public List<DocsPassResponseDto> docsPassList(String track){
        Track requestedTrack = validateTrackName(track);

        List<DocsPassResponseDto> result = new ArrayList<>();
        List<Object[]> joinerAndCandidateList = candidateRepository.findAllJoinerAndCandidateByDocs(Docs.PASS);

        if(track.equals("all")){
            for (Object[] objects : joinerAndCandidateList) {
                Joiner joiner = (Joiner) objects[0];
                Candidate candidate = (Candidate) objects[1];
                result.add(mapJoinerAndCandidateToResponse(joiner, candidate));
            }
        }else {
            for (Object[] objects : joinerAndCandidateList) {
                Joiner joiner = (Joiner) objects[0];
                Candidate candidate = (Candidate) objects[1];

                // 요청된 트랙과 joiner의 트랙이 일치하는 경우에만 결과를 추가
                if (joiner.getTrack().equals(requestedTrack)) {
                    DocsPassResponseDto dto = mapJoinerAndCandidateToResponse(joiner, candidate);
                    result.add(dto);
                }
            }
        }
        return result;
    }

    private void validateJoinerList(List<Joiner> joinerList){
        if(joinerList.isEmpty()){
            throw new NotFoundJoinerException("해당 트랙에 합격한 지원자가 존재하지 않습니다.");
        }
    }

    private InterviewPassResponseDto mapJoinerToResponse(Joiner joiner){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return InterviewPassResponseDto.builder()
                .joinerId(joiner.getId())
                .name(joiner.getName())
                .phoneNum(joiner.getPhoneNum())
                .studentID(joiner.getStudentId())
                .track(joiner.getTrack().getTrackName())
                .submissionTime(joiner.getCreatedAt().format(formatter))
                .build();
    }

    public List<InterviewPassResponseDto> interviewPassList(String track){
        Track requestedTrack = validateTrackName(track);

        // Candidate 테이블에서 Docs 값이 Pass이고 Interview 값이 PASS인 candidateList 추출
        List<Joiner> joinerList = joinerRepository.findAllById(
                candidateRepository.findAllByDocsAndInterview(Docs.PASS,Interview.PASS).stream()
                        .map(candidate -> candidate.getJoiner().getId())
                        .collect(Collectors.toSet())
        );

        //validateJoinerList(joinerList);
        if (joinerList == null) {
           return Collections.emptyList();
        }
        if(track.equals("all")){
            return joinerList.stream()
                    .map(this::mapJoinerToResponse)
                    .collect(Collectors.toList());
        }else {
            // 특정 track에 해당하는 Joiner만 필터링하여 최종 결과 매핑
            return joinerList.stream()
                    .filter(joiner -> joiner.getTrack().equals(requestedTrack))
                    .map(this::mapJoinerToResponse)
                    .collect(Collectors.toList());
        }
    }

    private DocsPassResponseDto mapJoinerAndCandidateToResponse(Joiner joiner, Candidate candidate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return DocsPassResponseDto.builder()
                .joinerId(joiner.getId())
                .name(joiner.getName())
                .phoneNum(joiner.getPhoneNum())
                .studentID(joiner.getStudentId())
                .track(joiner.getTrack().getTrackName())
                .interviewTime(candidate.getInterviewTime())
                .submissionTime(joiner.getCreatedAt().format(formatter))
                .build();
    }

    public String setInterviewTime(InterviewTimeRequest requestDto) {
        Joiner joiner = joinerRepository.findById(requestDto.getJoinerId())
                .orElseThrow(() -> new NotFoundJoinerException("해당 ID의 Joiner를 찾을 수 없습니다."));

        Candidate candidate = candidateRepository.findByJoiner(joiner)
                .orElseThrow(() -> new NotFoundCandidateException("해당 Joiner에 대한 Candidate를 찾을 수 없습니다."));

        candidate.setInterviewTime(requestDto.getInterviewDate() + " " + requestDto.getInterviewTime());

        candidateRepository.save(candidate);

        return "면접 시간이 성공적으로 설정되었습니다.";
    }

    public ApplicationStatusResponse getApplicationStatus(AuthInfo authInfo, String track, Pageable pageable) {
        if(!hasValidRoles(authInfo, List.of(Role.SUPERUSER, Role.MANAGER))) {
            throw new UnauthorizedRoleException("접근 권한이 없습니다.");
        }

        //트랙별 지원자수 조회
        Long countByTrackPM = joinerRepository.countByTrack(Track.PLANDESIGN);
        Long countByTrackFE = joinerRepository.countByTrack(Track.FRONTEND);
        Long countByTrackBE = joinerRepository.countByTrack(Track.BACKEND);
        Long countByTrackALL = countByTrackPM + countByTrackFE + countByTrackBE;

        ApplicationStatusByTrack applicationStatusByTrack = new ApplicationStatusByTrack(
                countByTrackALL, countByTrackPM, countByTrackFE, countByTrackBE
        );

        Track requestedTrack = validateTrackName(track);
        Page<Joiner> joinerPage = new PageImpl<>(List.of());
        if(requestedTrack.equals(Track.ALL)){
            joinerPage = joinerRepository.findAllByOrderByCreatedAt(pageable);
        }
        else{
            joinerPage = joinerRepository.findAllByTrackOrderByCreatedAtAsc(requestedTrack, pageable);
        }

        List<ApplicationDocumentPreview> applicationDocumentPreviewList = joinerPage.getContent().stream()
                .map(this::mapJoinerToApplicationDocumentPreview)
                .collect(Collectors.toList());

        return ApplicationStatusResponse.builder()
                .applicationStatusByTrack(applicationStatusByTrack)
                .applicationDocumentPreviewList(applicationDocumentPreviewList)
                .currentPage(joinerPage.getNumber())
                .totalPages(joinerPage.getTotalPages())
                .build();
    }

    private ApplicationDocumentPreview mapJoinerToApplicationDocumentPreview(Joiner joiner) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return ApplicationDocumentPreview.builder()
                .joinerId(joiner.getId())
                .name(joiner.getName())
                .phoneNum(joiner.getPhoneNum())
                .studentID(joiner.getStudentId())
                .track(joiner.getTrack().getTrackName())
                .submissionTime(joiner.getCreatedAt().format(formatter))
                .build();
    }

}
