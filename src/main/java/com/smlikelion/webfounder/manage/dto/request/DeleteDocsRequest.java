package com.smlikelion.webfounder.manage.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DeleteDocsRequest {

    @NotEmpty(message = "삭제할 joinerId 목록은 비어 있을 수 없습니다.")
    private List<Long> joinerIds;

    @Builder
    public DeleteDocsRequest(List<Long> joinerIds) {
        this.joinerIds = joinerIds;
    }
}