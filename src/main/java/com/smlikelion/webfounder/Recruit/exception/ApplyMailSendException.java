package com.smlikelion.webfounder.Recruit.exception;

import org.springframework.mail.MailSendException;

public class ApplyMailSendException extends RuntimeException {
    public ApplyMailSendException(String message) {
        super(message);
    }
}
