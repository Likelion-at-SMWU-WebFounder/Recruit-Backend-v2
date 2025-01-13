package com.smlikelion.webfounder.manage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApplicationStatusByTrack {
    private Long allApplicants;
    private Long pmApplicants;
    private Long feApplicants;
    private Long beApplicants;
}
