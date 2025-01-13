package com.smlikelion.webfounder.security.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EmptyTokenException extends RuntimeException{
    private String message;
}
