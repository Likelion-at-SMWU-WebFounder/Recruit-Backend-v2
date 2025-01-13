package com.smlikelion.webfounder.Recruit.Dto.Request;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class MailRequestDto {
    @Email(message = "이메일 형식에 맞지 않습니다.")
    @NotNull
    @NotBlank
    private String emailAdd;
}
