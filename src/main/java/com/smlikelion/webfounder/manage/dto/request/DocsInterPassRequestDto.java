package com.smlikelion.webfounder.manage.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class DocsInterPassRequestDto {
    @NotNull(message = "해당 지원자를 선택해주세요.")
    private List<Long> joinerIds;
}
