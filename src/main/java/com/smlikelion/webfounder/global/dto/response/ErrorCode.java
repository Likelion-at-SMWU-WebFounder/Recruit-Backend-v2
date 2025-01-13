package com.smlikelion.webfounder.global.dto.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    //success
    SUCCESS(true, HttpStatus.OK.value(), "요청에 성공했습니다."),

    //created
    CREATED(true, HttpStatus.CREATED.value(), "요청이 생성되었습니다."),

    //common
    INVALID_INPUT_VALUE_ERROR(false, HttpStatus.BAD_REQUEST.value(), "올바르지 않은 입력값입니다."),

    //internal
    INTERNAL_SERVER_ERROR(false,HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내부에서 문제가 발생했습니다."),
    NOT_FOUND(false, HttpStatus.NOT_FOUND.value(), "해당 정보는 존재하지 않습니다."),
    UNAUTHORIZED(false, HttpStatus.UNAUTHORIZED.value(), "권한이 없습니다."),
    S3_REGISTER_IMAGE_FAILURE_ERROR(false, HttpStatus.BAD_REQUEST.value(), "s3 이미지 저장 중 문제가 발생했습니다."),

    //auth
    EMAIL_EXISTS_ERROR(false, HttpStatus.BAD_REQUEST.value(), "이미 존재하는 이메일입니다."),
    INVALID_EMAIL_ERROR(false, HttpStatus.BAD_REQUEST.value(), "존재하지 않는 이메일 정보입니다."),
    INVALID_ACCESS_TOKEN_ERROR(false, HttpStatus.BAD_REQUEST.value(), "AccessToken 정보를 찾을 수 없습니다."),
    INVALID_REFRESH_TOKEN_ERROR(false, HttpStatus.BAD_REQUEST.value(), "RefreshToken 정보를 찾을 수 없습니다."),
    USERNAME_EXISTS_ERROR(false, HttpStatus.BAD_REQUEST.value(), "이미 존재하는 닉네임입니다."),
    UNAUTHORIZED_ERROR(false, HttpStatus.UNAUTHORIZED.value(), "로그인을 하지 않았습니다"),
    FORBIDDEN_ERROR(false, HttpStatus.FORBIDDEN.value(), "권한이 없습니다."),
    REFRESH_TOKEN_NOT_FOUND_ERROR(false, HttpStatus.BAD_REQUEST.value(), "RefreshToken 정보를 찾을 수 없습니다."),

    //admin
    ALREADY_EXISTS_ACCOUNT_ERROR(false, HttpStatus.BAD_REQUEST.value(), "이미 존재하는 아이디입니다."),
    ALREADY_EXISTS_NAME_ERROR(false, HttpStatus.BAD_REQUEST.value(), "이미 존재하는 이름입니다."),
    NOT_FOUND_ADMIN_ERROR(false, HttpStatus.BAD_REQUEST.value(), "해당 관리자ID를 찾을 수 없습니다."),
    UNSUPPORTED_ROLE_ERROR(false, HttpStatus.BAD_REQUEST.value(), "지원되지 않는 역할입니다."),
    INVALID_PASSWORD_ERROR(false, HttpStatus.BAD_REQUEST.value(), "비밀번호가 일치하지 않습니다."),
    UNAUTHORIZED_ROLE_ERROR(false, HttpStatus.FORBIDDEN.value(), "권한이 없습니다."),
    INVALID_TOKEN_ERROR(false, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 토큰입니다."),
    EMPTY_TOKEN_ERROR(false, HttpStatus.BAD_REQUEST.value(), "토큰이 비어있습니다."),
    EXPIRED_TOKEN_ERROR(false, HttpStatus.UNAUTHORIZED.value(), "만료된 토큰입니다."),

    //manage
    UNSUPPORTED_TRACK_ERROR(false, HttpStatus.BAD_REQUEST.value(), "지원되지 않는 트랙입니다."),
    MISMATCHED_YEAR_ERROR(false, HttpStatus.BAD_REQUEST.value(), "해당 년도와 일치하지 않습니다."),
    ALREADY_EXISTS_QUESTION_NUMBER_ERROR(false, HttpStatus.BAD_REQUEST.value(), "이미 해당 년도, 해당 트랙에 존재하는 문항 번호입니다."),
    NOT_FOUND_QUESTION_ERROR(false, HttpStatus.BAD_REQUEST.value(), "해당 문항을 찾을 수 없습니다."),
    MISMATCHED_TRACK_ERROR(false, HttpStatus.BAD_REQUEST.value(), "해당 트랙과 일치하지 않습니다."),
    INVALID_INTERVIEW_PASS_ERROR(false,HttpStatus.BAD_REQUEST.value(),"서류 불합격자가 포함되어 있습니다."),
    DELETE_ENTITY_ERROR(false, HttpStatus.BAD_REQUEST.value(), "서류 전체 삭제 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요"),

    //result
    MISSING_FIELD_ERROR(false, HttpStatus.BAD_REQUEST.value(), "입력란을 비워서 요청하는 경우입니다."),
    PASSWORD_INCORRECT_ERROR(false, HttpStatus.BAD_REQUEST.value(), "비밀번호가 일치하지 않습니다."),
    MEMBER_NOT_FOUND_ERROR(false, HttpStatus.BAD_REQUEST.value(), "존재하지 않는 회원 정보입니다."),

    //mail
    APPLY_MAIL_SEND_ERROR(false, HttpStatus.BAD_REQUEST.value(), "지원서 제출 메일 전송 중 문제가 발생했습니다. 이메일을 다시 확인해주세요."),

    DUPLICATE_STUDENT_ID_ERROR(false, HttpStatus.CONFLICT.value(), "동일한 학번으로 중복된 지원서가 이미 제출되었습니다.");


    private Boolean isSuccess;
    private int code;
    private String message;

    ErrorCode(Boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }

}
