package com.smlikelion.webfounder.manage.exception;

import com.smlikelion.webfounder.global.dto.response.BaseResponse;
import com.smlikelion.webfounder.global.dto.response.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class ManageExceptionHandler {
    @ExceptionHandler(UnsupportedTrackException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<?> handleUnsupportedTrackException(UnsupportedTrackException e, HttpServletRequest request) {
        log.warn("MANAGE-001> 요청 URI: " + request.getRequestURI() + ", 에러메세지: " + e.getMessage());
        return new BaseResponse<>(ErrorCode.UNSUPPORTED_TRACK_ERROR);
    }

    @ExceptionHandler(MismatchedYearException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<?> handleMismatchedYearException(MismatchedYearException e, HttpServletRequest request) {
        log.warn("MANAGE-002> 요청 URI: " + request.getRequestURI() + ", 에러메세지: " + e.getMessage());
        return new BaseResponse<>(ErrorCode.MISMATCHED_YEAR_ERROR);
    }

    @ExceptionHandler(AlreadyExistsQuestionNumberException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<?> handleAlreadyExistsQuestionNumberException(AlreadyExistsQuestionNumberException e, HttpServletRequest request) {
        log.warn("MANAGE-003> 요청 URI: " + request.getRequestURI() + ", 에러메세지: " + e.getMessage());
        return new BaseResponse<>(ErrorCode.ALREADY_EXISTS_QUESTION_NUMBER_ERROR);
    }

    @ExceptionHandler(NotFoundQuestionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<?> handleNotFoundQuestionException(NotFoundQuestionException e, HttpServletRequest request) {
        log.warn("MANAGE-004> 요청 URI: " + request.getRequestURI() + ", 에러메세지: " + e.getMessage());
        return new BaseResponse<>(ErrorCode.NOT_FOUND_QUESTION_ERROR);
    }

    @ExceptionHandler(MismatchedTrackException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<?> handleMismatchedTrackException(MismatchedTrackException e, HttpServletRequest request) {
        log.warn("MANAGE-005> 요청 URI: " + request.getRequestURI() + ", 에러메세지: " + e.getMessage());
        return new BaseResponse<>(ErrorCode.MISMATCHED_TRACK_ERROR);
    }

    @ExceptionHandler(NotFoundJoinerException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BaseResponse<?> handleNotFoundQuestionException(NotFoundJoinerException e, HttpServletRequest request) {
        log.warn("MANAGE-006> 요청 URI: " + request.getRequestURI() + ", 에러메세지: " + e.getMessage());
        return new BaseResponse<>(ErrorCode.NOT_FOUND);
    }

    @ExceptionHandler(NotFoundCandidateException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BaseResponse<?> handleNotFoundQuestionException(NotFoundCandidateException e, HttpServletRequest request) {
        log.warn("MANAGE-007> 요청 URI: " + request.getRequestURI() + ", 에러메세지: " + e.getMessage());
        return new BaseResponse<>(ErrorCode.NOT_FOUND);
    }

    @ExceptionHandler(InternalServerCandidateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse<?> handleNotFoundQuestionException(InternalServerCandidateException e, HttpServletRequest request) {
        log.warn("MANAGE-008> 요청 URI: " + request.getRequestURI() + ", 에러메세지: " + e.getMessage());
        return new BaseResponse<>(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidInterviewPassException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<?> handleNotFoundQuestionException(InvalidInterviewPassException e, HttpServletRequest request) {
        log.warn("MANAGE-009> 요청 URI: " + request.getRequestURI() + ", 에러메세지: " + e.getMessage());
        return new BaseResponse<>(ErrorCode.INVALID_INTERVIEW_PASS_ERROR);
    }

    @ExceptionHandler(DeleteEntityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<?> handleDeleteEntityException(DeleteEntityException e, HttpServletRequest request) {
        log.warn("MANAGE-010> 요청 URI: " + request.getRequestURI() + ", 에러메세지: " + e.getMessage());
        return new BaseResponse<>(ErrorCode.DELETE_ENTITY_ERROR);
    }


}
