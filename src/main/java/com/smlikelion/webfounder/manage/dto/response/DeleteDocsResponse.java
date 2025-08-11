package com.smlikelion.webfounder.manage.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DeleteDocsResponse {
    private final int requested;
    private final int deleted;
    private final List<Long> failed;

    @Builder
    public DeleteDocsResponse(int requested, int deleted, List<Long> failed) {
        this.requested = requested;
        this.deleted = deleted;
        this.failed = failed;
    }
}