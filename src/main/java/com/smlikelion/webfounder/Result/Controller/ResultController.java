package com.smlikelion.webfounder.Result.Controller;

import com.smlikelion.webfounder.Result.Dto.Request.ResultDocsRequest;
import com.smlikelion.webfounder.Result.Dto.Request.ResultInterviewRequest;
import com.smlikelion.webfounder.Result.Dto.Response.ResultDocsResponse;
import com.smlikelion.webfounder.Result.Dto.Response.ResultInterviewResponse;
import com.smlikelion.webfounder.Result.Service.ResultService;
import com.smlikelion.webfounder.global.dto.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/recruit/result")
@RequiredArgsConstructor
public class ResultController {

    private final ResultService resultService;

    @Operation(summary = "1차 심사 결과 확인")
    @PostMapping("/docs")
    public BaseResponse<ResultDocsResponse> getResultDocs(
            @RequestBody @Valid ResultDocsRequest request) {
        return resultService.getDocsResult(request);
    }
    @Operation(summary = "2차 면접 결과 확인")
    @PostMapping("/interview")
    public BaseResponse<ResultInterviewResponse> getResultInterview(
            @RequestBody @Valid ResultInterviewRequest request) {
        return resultService.getInterviewResult(request);
    }
}
