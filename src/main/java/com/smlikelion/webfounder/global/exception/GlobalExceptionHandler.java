package com.smlikelion.webfounder.global.exception;

import com.smlikelion.webfounder.Recruit.exception.DuplicateStudentIdException;
import com.smlikelion.webfounder.global.dto.response.BaseResponse;
import com.smlikelion.webfounder.global.dto.response.ErrorCode;
import com.smlikelion.webfounder.global.dto.response.GlobalExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateStudentIdException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public BaseResponse<?> handleDuplicateStudentIdException(DuplicateStudentIdException ex, HttpServletRequest request) {
        log.warn("GLOBAL-002> 요청 URI: " + request.getRequestURI() + ", 에러메세지: " + ex.getMessage());
        return new BaseResponse<>(ErrorCode.DUPLICATE_STUDENT_ID_ERROR, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<?> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        BindingResult bindingResult = ex.getBindingResult();

        if (bindingResult.hasErrors()) {
            List<GlobalExceptionResponse> globalExceptionResponseList = new ArrayList<>();
            bindingResult.getFieldErrors().forEach(fieldError -> {
                GlobalExceptionResponse globalExceptionResponse = new GlobalExceptionResponse(
                        fieldError.getField(),
                        fieldError.getDefaultMessage()
                );
                globalExceptionResponseList.add(globalExceptionResponse);
            });


            log.warn("GLOBAL-001> 요청 URI: " + request.getRequestURI() + ", 에러메세지: " + "Invalid input value");
            return new BaseResponse<>(ErrorCode.INVALID_INPUT_VALUE_ERROR, globalExceptionResponseList);
        }

        log.warn("GLOBAL-001> 요청 URI: " + request.getRequestURI() + ", 에러메세지: " + "Invalid input value");
        return new BaseResponse<>(ErrorCode.INVALID_INPUT_VALUE_ERROR, "Invalid input value");
    }
}

//{field: , content: }