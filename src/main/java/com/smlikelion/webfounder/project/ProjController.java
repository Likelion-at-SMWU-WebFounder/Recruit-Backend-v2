package com.smlikelion.webfounder.project;

import com.smlikelion.webfounder.global.dto.response.BaseResponse;
import com.smlikelion.webfounder.global.dto.response.ErrorCode;
import com.smlikelion.webfounder.project.dto.ProjListResponseDto;
import com.smlikelion.webfounder.project.dto.ProjRequestDto;
import com.smlikelion.webfounder.project.dto.ProjResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/project")
public class ProjController {
    private final ProjectService projectService;

    // [POST] 프로젝트 작성
    @PostMapping()
    public BaseResponse<ProjRequestDto>  createProj(@RequestBody @Valid ProjRequestDto requestDto){
        ProjRequestDto project = projectService.createProj(requestDto);
        return new BaseResponse<>(ErrorCode.CREATED,project);
    }

    // [GET] 프로젝트 전체 조회
    @GetMapping()
    public BaseResponse<List<ProjResponseDto>> getAllProj(){
        return new BaseResponse<>(projectService.findAllProj());
    }

    // [GET] 프로젝트 상세 조회
    @GetMapping("/{id}")
    public BaseResponse<ProjListResponseDto> getOneProj(@PathVariable Long id){
        return new BaseResponse<>(projectService.findOneProj(id));
    }

    // [DELETE] 프로젝트 삭제
    @DeleteMapping("/{id}")
    public BaseResponse<Long> delProj(@PathVariable Long id){
        return new BaseResponse<>(projectService.delProj(id));
    }

    // [PUT] 프로젝트 수정
    @PutMapping("/{id}")
    public BaseResponse<ProjListResponseDto> updateProj(@PathVariable Long id, @RequestBody @Valid ProjRequestDto requestDto){
        return new BaseResponse<>(projectService.updateProj(id, requestDto));
    }
}

