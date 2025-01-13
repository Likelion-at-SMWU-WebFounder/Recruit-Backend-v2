package com.smlikelion.webfounder.manage.dto.request;

import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class DocsQuestUpdateRequest {
    @NotNull(message = "Id를 입력 해야 합니다.")
    private Long id;

    @NotNull(message = "활동 기수 입력을 해야 합니다.")
    @Min(value = 2023, message = "2023년 보다 작습니다.")
    @Max(value = 2123, message = "2123년 보다 큽니다.")
    private Long year;

    @NotNull(message = "문항 번호 입력을 해야 합니다.")
    @Min(value = 1, message = "최소 문항 번호는 1입니다.")
    @Max(value = 10, message = "최대 문항 번호는 10입니다.")
    private Long number;

    @NotBlank(message = "문항 내용 입력을 해야 합니다.")
    private String content;

    @NotNull(message = "최대 글자 수 입력을 해야 합니다.")
    private Long maxLength;

    @NotBlank(message = "트랙 입력을 해야 합니다.")
    private String track; //pm, fe, be, common, all
}
