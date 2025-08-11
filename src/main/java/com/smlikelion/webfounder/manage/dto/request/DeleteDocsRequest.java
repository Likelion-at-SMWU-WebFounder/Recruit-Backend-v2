package com.smlikelion.webfounder.manage.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DeleteDocsRequest {
    @NotEmpty(message = "삭제할 문서 ID 목록은 비어 있을 수 없습니다.")
    private final List<Long> docIds;

    @Builder
    public DeleteDocsRequest(List<Long> docIds) {
        this.docIds = docIds;
    }
}
