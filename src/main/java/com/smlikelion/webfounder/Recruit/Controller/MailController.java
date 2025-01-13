package com.smlikelion.webfounder.Recruit.Controller;

import com.smlikelion.webfounder.Recruit.Dto.Request.MailRequestDto;
import com.smlikelion.webfounder.Recruit.Service.MailService;
import com.smlikelion.webfounder.global.dto.response.BaseResponse;
import com.smlikelion.webfounder.global.dto.response.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/recruit")
@Slf4j
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    @Operation(summary = "지원자 메일 제출")
    @PostMapping("/mail")
    private BaseResponse<String> mailSubmit(@RequestBody @Valid MailRequestDto requestDto){
        return new BaseResponse<>(ErrorCode.CREATED, mailService.mailSubmit(requestDto)+" 등록되었습니다.");
    }

    @Operation(summary = "지원자 메일 전체 조회")
    @GetMapping("/mail")
    private BaseResponse<List<String>> mailList(){
        return new BaseResponse<>(mailService.findAllmail());
    }

    @Operation(summary = "관리자 메일 전송")
    @PostMapping("/mail/send")
    private BaseResponse<String> mailSend(){
        return new BaseResponse<>(ErrorCode.SUCCESS, mailService.sendMail());
    }

}
