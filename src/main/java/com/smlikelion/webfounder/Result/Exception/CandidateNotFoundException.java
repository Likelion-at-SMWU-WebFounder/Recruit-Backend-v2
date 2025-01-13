package com.smlikelion.webfounder.Result.Exception;

import com.smlikelion.webfounder.global.dto.response.ErrorCode;

public class CandidateNotFoundException extends RuntimeException {
    public CandidateNotFoundException(String message) {
        super(message);
    }

    public ErrorCode toErrorCode() {
        return ErrorCode.NOT_FOUND;
    }
}