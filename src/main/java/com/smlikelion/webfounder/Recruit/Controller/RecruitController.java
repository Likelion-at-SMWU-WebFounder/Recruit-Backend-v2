package com.smlikelion.webfounder.Recruit.Controller;

import com.smlikelion.webfounder.Recruit.Dto.Request.RecruitmentRequest;
import com.smlikelion.webfounder.Recruit.Dto.Response.RecruitmentResponse;
import com.smlikelion.webfounder.Recruit.Entity.Joiner;
import com.smlikelion.webfounder.Recruit.Repository.JoinerRepository;
import com.smlikelion.webfounder.Recruit.Service.RecruitService;
import com.smlikelion.webfounder.Recruit.exception.DuplicateStudentIdException;
import com.smlikelion.webfounder.global.dto.response.BaseResponse;
import com.smlikelion.webfounder.global.dto.response.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/recruit")
@Slf4j
public class RecruitController {

    @Autowired
    private RecruitService recruitService;

    @Autowired
    private JoinerRepository joinerRepository;

    @Value("${GOOGLE_DOCS_DOCUMENT_ID}")
    private String documentId;


    @Operation(summary = "트랙별 서류 작성하기 및 Google Docs 자동 업로드")
    @PostMapping("/docs")
    public BaseResponse<RecruitmentResponse> submitRecruitment(
            @RequestParam("track") String track,
            @RequestBody @Valid RecruitmentRequest request,
            BindingResult bindingResult) {

        logValidationErrors(bindingResult);

        if ("fe".equalsIgnoreCase(track) || "pm".equalsIgnoreCase(track) || "be".equalsIgnoreCase(track)) {
            try {
                // 서류 등록
                RecruitmentResponse recruitResponse = recruitService.registerRecruitment(request);

                // Google Docs 업로드
                recruitService.uploadToGoogleDocs(documentId, request);
                log.info("Google Docs에 서류가 정상적으로 업로드됨: {}", documentId);

                return new BaseResponse<>(recruitResponse);
            } catch (DuplicateStudentIdException e) {
                return new BaseResponse<>(ErrorCode.NOT_FOUND.getCode(), ErrorCode.DUPLICATE_STUDENT_ID_ERROR.getMessage(), null);
            } catch (Exception e) {
                log.error("Google Docs 업로드 중 오류 발생", e);
                return new BaseResponse<>(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), "Google Docs update failed", null);
            }
        } else {
            // 유효하지 않은 트랙 값 처리
            String errorMessage = "Invalid track value. Please provide a valid track (fe, pm, be).";
            return new BaseResponse<>(ErrorCode.NOT_FOUND.getCode(), errorMessage, null);
        }
    }

    @Operation(summary = "트랙별 서류 작성 페이지 조회하기")
    @GetMapping("/docs/{joinerId}")
    public BaseResponse<RecruitmentResponse> getJoinerDetails(
            @PathVariable Long joinerId) {
        Joiner joiner = joinerRepository.findById(joinerId).orElse(null);

        if (joiner != null) {
            // Joiner를 찾은 경우, RecruitmentResponse로 변환하여 응답 반환
            RecruitmentResponse recruitResponse = RecruitmentResponse.builder()
                    .id(joiner.getId())
                    .studentInfo(joiner.toStudentInfoResponse())
                    .answerList(joiner.toAnswerListResponse())
                    .interviewTime(joiner.getInterviewTimeValues()) // 필요에 따라 수정
                    .build();

            return new BaseResponse<>(recruitResponse);
        } else {
            // Joiner를 찾지 못한 경우, 오류 응답 반환
            return new BaseResponse<>(ErrorCode.NOT_FOUND);
        }
    }

    @Operation(summary = "트랙별 서류 결과 합격자 조회하기")
    @PostMapping("/docs/result")
    private void logValidationErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            errors.forEach(error -> log.error("Validation error: {}", error.getDefaultMessage()));
        }
    }

    @Operation(summary = "트랙별 서류를 Google Docs에 추가")
    @PostMapping("/docs/upload")
    public BaseResponse<String> uploadToExistingGoogleDoc(
            @RequestParam("documentId") String documentId,
            @RequestBody @Valid RecruitmentRequest request,
            BindingResult bindingResult) {

        logValidationErrors(bindingResult);

        try {
            String docId = recruitService.uploadToGoogleDocs(documentId, request);
            return new BaseResponse<>("Content added to document successfully with ID: " + docId);
        } catch (Exception e) {
            return new BaseResponse<>(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), "Google Docs update failed", null);
        }
    }
}
