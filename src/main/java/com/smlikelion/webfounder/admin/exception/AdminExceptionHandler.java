package com.smlikelion.webfounder.admin.exception;

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
public class AdminExceptionHandler {
    @ExceptionHandler(AlreadyExistsAccountException.class)
    public BaseResponse<?> handleAlreadyExistsAdminException(AlreadyExistsAccountException e, HttpServletRequest request) {
        log.warn("ADMIN-001> 요청 URI: " + request.getRequestURI() + ", 에러메세지: " + e.getMessage());
        return new BaseResponse<>(ErrorCode.ALREADY_EXISTS_ACCOUNT_ERROR);
    }

    @ExceptionHandler(AlreadyExistsNameException.class)
    public BaseResponse<?> handleAlreadyExistsNameException(AlreadyExistsNameException e, HttpServletRequest request) {
        log.warn("ADMIN-002> 요청 URI: " + request.getRequestURI() + ", 에러메세지: " + e.getMessage());
        return new BaseResponse<>(ErrorCode.ALREADY_EXISTS_NAME_ERROR);
    }

    @ExceptionHandler(NotFoundAdminException.class)
    public BaseResponse<?> handleNotFoundAdminException(NotFoundAdminException e, HttpServletRequest request) {
        log.warn("ADMIN-003> 요청 URI: " + request.getRequestURI() + ", 에러메세지: " + e.getMessage());
        return new BaseResponse<>(ErrorCode.NOT_FOUND_ADMIN_ERROR);
    }

    @ExceptionHandler(UnsupportedRoleException.class)
    public BaseResponse<?> handleUnsupportedRoleException(UnsupportedRoleException e, HttpServletRequest request) {
        log.warn("ADMIN-004> 요청 URI: " + request.getRequestURI() + ", 에러메세지: " + e.getMessage());
        return new BaseResponse<>(ErrorCode.UNSUPPORTED_ROLE_ERROR);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public BaseResponse<?> handleInvalidPasswordException(InvalidPasswordException e, HttpServletRequest request) {
        log.warn("ADMIN-005> 요청 URI: " + request.getRequestURI() + ", 에러메세지: " + e.getMessage());
        return new BaseResponse<>(ErrorCode.INVALID_PASSWORD_ERROR);
    }

    @ExceptionHandler(UnauthorizedRoleException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public BaseResponse<?> handleUnauthorizedRoleException(UnauthorizedRoleException e, HttpServletRequest request) {
        log.warn("ADMIN-006> 요청 URI: " + request.getRequestURI() + ", 에러메세지: " + e.getMessage());
        return new BaseResponse<>(ErrorCode.UNAUTHORIZED_ROLE_ERROR);
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public BaseResponse<?> handleInvalidTokenException(RefreshTokenNotFoundException e, HttpServletRequest request) {
        log.warn("ADMIN-007> 요청 URI: " + request.getRequestURI() + ", 에러메세지: " + e.getMessage());
        return new BaseResponse<>(ErrorCode.REFRESH_TOKEN_NOT_FOUND_ERROR);
    }

}
