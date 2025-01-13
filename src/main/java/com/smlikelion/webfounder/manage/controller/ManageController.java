package com.smlikelion.webfounder.manage.controller;

import com.smlikelion.webfounder.Recruit.Repository.JoinerRepository;
import com.smlikelion.webfounder.global.dto.response.BaseResponse;
import com.smlikelion.webfounder.global.dto.response.ErrorCode;
import com.smlikelion.webfounder.manage.dto.request.DocsInterPassRequestDto;
import com.smlikelion.webfounder.manage.dto.request.DocsQuestRequest;
import com.smlikelion.webfounder.manage.dto.request.DocsQuestUpdateRequest;
import com.smlikelion.webfounder.manage.dto.request.InterviewTimeRequest;
import com.smlikelion.webfounder.manage.dto.response.ApplicationStatusResponse;
import com.smlikelion.webfounder.manage.dto.response.DocsPassResponseDto;
import com.smlikelion.webfounder.manage.dto.response.DocsQuestResponse;
import com.smlikelion.webfounder.manage.dto.response.InterviewPassResponseDto;
import com.smlikelion.webfounder.manage.service.ManageService;
import com.smlikelion.webfounder.manage.service.SQLExecutionService;
import com.smlikelion.webfounder.security.Auth;
import com.smlikelion.webfounder.security.AuthInfo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.smlikelion.webfounder.Recruit.Entity.Joiner;


import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/manage")
@RequiredArgsConstructor
public class ManageController {

    private final ManageService manageService;
    private final JoinerRepository joinerRepository;
    private final SQLExecutionService sqlExecutionService;

    @Operation(summary = "서류 질문 등록하기")
    @PostMapping("/docs/quest")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<DocsQuestResponse> registerQuestion(
            @Auth AuthInfo authInfo,
            @RequestBody @Valid DocsQuestRequest request) {
        return new BaseResponse<>(manageService.registerQuestion(authInfo, request));
    }

    @Operation(summary = "서류 문항 조회하기")
    @GetMapping("/docs/quest")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<List<DocsQuestResponse>> retrieveQuestionByYearAndTrack(
            @Auth AuthInfo authInfo,
            @RequestParam("year") Long year,
            @RequestParam("track") String track) {
        return new BaseResponse<>(manageService.retrieveQuestionByYearAndTrack(authInfo, year, track));
    }

    @Operation(summary = "서류 질문 삭제하기")
    @DeleteMapping("/docs/quest/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<DocsQuestResponse> deleteQuestion(
            @Auth AuthInfo authInfo,
            @PathVariable("id") Long id) {
        return new BaseResponse<>(manageService.deleteQuestion(authInfo, id));
    }

    @Operation(summary = "서류 질문 수정하기")
    @PutMapping("/docs/quest/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<DocsQuestResponse> updateQuestion(
            @Auth AuthInfo authInfo,
            @PathVariable("id") Long id,
            @RequestBody @Valid DocsQuestRequest request) {
        return new BaseResponse<>(manageService.updateQuestion(authInfo, id, request));
    }

    @Operation(summary = "서류 질문 여러개 수정하기")
    @PutMapping("/docs/quests")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<List<DocsQuestResponse>> updateQuestions(
            @Auth AuthInfo authInfo,
            @RequestBody @Valid List<DocsQuestUpdateRequest> requests) {
        return new BaseResponse<>(manageService.updateQuestions(authInfo, requests));
    }

    @Operation(summary = "서류 합격자 선정")
    @PostMapping("/docs/add")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<String> docsPass(@RequestBody DocsInterPassRequestDto requestDto){
        return new BaseResponse<>(manageService.docsPass(requestDto)+"번 지원자가 서류 합격자 선정되었습니다.");
    }

    @Operation(summary = "서류 합격자 취소")
    @DeleteMapping("/docs/del")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<String> docsFail(@RequestBody DocsInterPassRequestDto requestDto){
        return new BaseResponse<>(manageService.docsFail(requestDto)+"번 지원자가 서류 합격자 취소되었습니다.");
    }

    @Operation(summary = "면접 합격자 선정")
    @PostMapping("/interview/add")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<String> interviewPass(@RequestBody DocsInterPassRequestDto requestDto){
        return new BaseResponse<>(manageService.interviewPass(requestDto)+"번 지원자가 면접 합격자 선정되었습니다.");
    }

    @Operation(summary = "면접 합격자 취소")
    @DeleteMapping("/interview/del")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<String> interviewFail(@RequestBody DocsInterPassRequestDto requestDto){
        return new BaseResponse<>(manageService.interviewFail(requestDto)+"번 지원자가 면접 합격자 취소되었습니다.");
    }

    @Operation(summary = "서류 합격자 전체 조회")
    @GetMapping("/docs/result")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<List<DocsPassResponseDto>> docsPassList(@RequestParam("track") String track){
        return new BaseResponse<>(manageService.docsPassList(track));
    }

    @Operation(summary = "면접 합격자 전체 조회")
    @GetMapping("/interview/result")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<List<InterviewPassResponseDto>> interviewPassList(@RequestParam("track") String track){
        return new BaseResponse<>(manageService.interviewPassList(track));
    }

    @Operation(summary = "서류 합격자 면접 시간 등록")
    @PostMapping("/interviewtime")

    public BaseResponse<String> setInterviewTime(@RequestBody InterviewTimeRequest requestDto) {
        String message = manageService.setInterviewTime(requestDto);
        return new BaseResponse<>(message);
    }

    @Operation(summary = "서류 합격자 면접 시간 수정")
    @PutMapping("/interviewtime")
    public BaseResponse<String> updateInterviewTime(@RequestBody InterviewTimeRequest request) {
        String message = manageService.setInterviewTime(request);
        return new BaseResponse<>(HttpStatus.OK.value(), "면접 시간 수정", message);
    }

    @Operation(summary = "지원자 희망 면접 시간 조회하기")
    @GetMapping("/interviewtime/{joinerId}")
    public BaseResponse<Map<String, String>> getInterviewTime(
            @PathVariable Long joinerId) {
        Joiner joiner = joinerRepository.findById(joinerId).orElse(null);
        if (joiner != null) {
            return new BaseResponse<>(joiner.getInterviewTime());
        } else {
            // Joiner를 찾지 못한 경우, 오류 응답 반환
            return new BaseResponse<>(ErrorCode.NOT_FOUND);
        }
    }

    @Operation(summary = "지원현황 및 지원서류 조회하기")
    @GetMapping("/apply")
    public BaseResponse<ApplicationStatusResponse> getApplicationStatus(
            @Auth AuthInfo authInfo,
            @RequestParam("track") String track,
            @RequestParam(value="page", required = false, defaultValue = "0") int page,
            @RequestParam(value="size", required = false, defaultValue = "10") int size) {
        if( page < 0 || size <= 0) {
            page = 0;
            size = 10;
        }
        Pageable pageable = PageRequest.of(page, size);
        return new BaseResponse<>(manageService.getApplicationStatus(authInfo, track, pageable));
    }

    @Operation(summary = "지원서류 전부 삭제하기")
    @DeleteMapping("/apply/docs")
    public BaseResponse<String> deleteAllDocs(@Auth AuthInfo authInfo) {
        return new BaseResponse<>(sqlExecutionService.deleteAllDocs(authInfo));
    }
}
