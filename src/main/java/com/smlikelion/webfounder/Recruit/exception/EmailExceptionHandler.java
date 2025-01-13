package com.smlikelion.webfounder.Recruit.exception;

import com.smlikelion.webfounder.global.dto.response.BaseResponse;
import com.smlikelion.webfounder.global.dto.response.ErrorCode;
import com.smlikelion.webfounder.project.exception.NotfoundProjException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class EmailExceptionHandler {
    @ExceptionHandler(NotFoundEmailException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BaseResponse<?> handleNotFoundException(NotFoundEmailException e, HttpServletRequest request) {
        log.warn("Email-001> 요청 URI: " + request.getRequestURI() + ", 에러메세지: " + e.getMessage());
        return new BaseResponse<>(ErrorCode.NOT_FOUND);
    }

    @ExceptionHandler(ApplyMailSendException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<?> handleApplyMailSendException(ApplyMailSendException e, HttpServletRequest request) {
        log.error("Email-002> 요청 URI: " + request.getRequestURI() + ", 에러메세지: " + e.getMessage());
        return new BaseResponse<>(ErrorCode.APPLY_MAIL_SEND_ERROR);
    }
}
