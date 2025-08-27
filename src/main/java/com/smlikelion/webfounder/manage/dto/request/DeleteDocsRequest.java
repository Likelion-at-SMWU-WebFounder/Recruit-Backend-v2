package com.smlikelion.webfounder.manage.dto.request;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteDocsRequest {

    @NotEmpty(message = "삭제할 joinerId 목록은 비어 있을 수 없습니다.")
    private List<Long> joinerIds;

    @Builder
    public DeleteDocsRequest(List<Long> joinerIds) {
        this.joinerIds = joinerIds;
    }
}