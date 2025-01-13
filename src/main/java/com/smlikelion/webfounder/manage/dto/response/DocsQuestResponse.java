package com.smlikelion.webfounder.manage.dto.response;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class DocsQuestResponse {
    private Long id;
    private String content;
    private Long year;
    private String track;
    private Long number;
    private Long maxLength;
}
