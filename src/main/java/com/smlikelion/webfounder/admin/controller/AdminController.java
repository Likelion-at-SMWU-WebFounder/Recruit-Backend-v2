package com.smlikelion.webfounder.admin.controller;

import com.smlikelion.webfounder.admin.dto.request.SignInRequest;
import com.smlikelion.webfounder.admin.dto.request.SignUpRequest;
import com.smlikelion.webfounder.admin.dto.request.UpdateRoleRequest;
import com.smlikelion.webfounder.admin.dto.response.ReissueResponse;
import com.smlikelion.webfounder.admin.dto.response.SignInResponse;
import com.smlikelion.webfounder.admin.dto.response.SignUpResponse;
import com.smlikelion.webfounder.admin.dto.response.UpdateRoleResponse;
import com.smlikelion.webfounder.admin.service.AdminService;
import com.smlikelion.webfounder.global.dto.response.BaseResponse;
import com.smlikelion.webfounder.security.Auth;
import com.smlikelion.webfounder.security.AuthInfo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary="관리자 회원가입")
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<SignUpResponse> signUp(
            @RequestBody @Valid SignUpRequest request) {
        return new BaseResponse<>(adminService.signUp(request));
    }

    @Operation(summary="유저 역할부여")
    @PutMapping("/roles")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<UpdateRoleResponse> updateRoles(
            @Auth AuthInfo authInfo,
            @RequestBody @Valid UpdateRoleRequest request) {
        return new BaseResponse<>(adminService.updateRoles(authInfo, request));
    }

    @Operation(summary="관리자 로그인")
    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<SignInResponse> signIn(
            @RequestBody @Valid SignInRequest request) {
        return new BaseResponse<>(adminService.signIn(request));
    }

    @Operation(summary="토큰 유효성 확인")
    @PostMapping("/check-token-validation")
    public BaseResponse<Boolean> checkTokenValidation(@RequestBody String refreshToken) {
       return new BaseResponse<>(adminService.checkTokenValidation(refreshToken));
    }

    @Operation(summary="액세스 토큰 재발급")
    @PostMapping("/reissue")
    public BaseResponse<ReissueResponse> reissue(@Auth AuthInfo authInfo) {
        return new BaseResponse<>(adminService.reissue(authInfo.getAccountId(), authInfo.getToken()));
    }

    @Operation(summary="로그아웃")
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<String> logOut(@Auth AuthInfo authInfo) {
        adminService.logOut(authInfo.getAccountId());
        return new BaseResponse<>("로그아웃 성공");
    }

}
