package com.smlikelion.webfounder.manage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class ApplicationStatusResponse {
    private ApplicationStatusByTrack applicationStatusByTrack;
    private List<ApplicationDocumentPreview> applicationDocumentPreviewList;
    private int currentPage;
    private int totalPages;
}
