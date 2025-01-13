package com.smlikelion.webfounder.admin.service;

import com.smlikelion.webfounder.admin.dto.request.SignInRequest;
import com.smlikelion.webfounder.admin.dto.request.SignUpRequest;
import com.smlikelion.webfounder.admin.dto.request.UpdateRoleRequest;
import com.smlikelion.webfounder.admin.dto.response.ReissueResponse;
import com.smlikelion.webfounder.admin.dto.response.SignInResponse;
import com.smlikelion.webfounder.admin.dto.response.SignUpResponse;
import com.smlikelion.webfounder.admin.dto.response.UpdateRoleResponse;
import com.smlikelion.webfounder.admin.entity.Admin;
import com.smlikelion.webfounder.admin.entity.Block;
import com.smlikelion.webfounder.admin.entity.Role;
import com.smlikelion.webfounder.admin.exception.*;
import com.smlikelion.webfounder.admin.repository.AdminRepository;
import com.smlikelion.webfounder.security.AuthInfo;
import com.smlikelion.webfounder.security.JwtTokenProvider;
import com.smlikelion.webfounder.security.TokenInfo;
import com.smlikelion.webfounder.security.exception.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

   private final AdminRepository adminRepository;
   private final PasswordEncoder passwordEncoder;
   private final JwtTokenProvider tokenProvider;

   public void createSuperUser() {
       if(adminRepository.existsByRole(Role.SUPERUSER)) {
           log.info("SuperUser already exists");
           return;
       }

       Admin admin = adminRepository.save(
               Admin.builder()
                       .accountId("smlikelion")
                       .password(passwordEncoder.encode("smlikelion1234"))
                       .name("admin")
                       .role(Role.SUPERUSER)
                       .block(Block.ISACTIVE)
                       .build()
       );

       log.info("SuperUser created: {}", admin);
   }

    @Transactional
    public UpdateRoleResponse updateRoles(AuthInfo authInfo, UpdateRoleRequest request) {
        if(!authInfo.getRoles().get(0).equals(Role.SUPERUSER)) {
            throw new UnauthorizedRoleException("권한이 없습니다.");
        }

        Admin admin = adminRepository.findByAdminIdAndAccountId(request.getId(), request.getAccountId())
                .orElseThrow(() -> new NotFoundAdminException("해당하는 아이디가 없습니다."));
        if(request.getRole().toUpperCase().equals(Role.MANAGER.name())) {
            admin.setRole(Role.MANAGER);
            adminRepository.save(admin);
        } else if(request.getRole().toUpperCase().equals(Role.USER.name())) {
            admin.setRole(Role.USER);
            adminRepository.save(admin);
        } else {
            throw new UnsupportedRoleException("해당하는 역할이 없습니다.");
        }

        return mapAdminToUpdateRoleResponse(admin);
    }
    public SignUpResponse signUp(SignUpRequest request) {
        if(adminRepository.existsByAccountId(request.getAccountId())) {
            throw new AlreadyExistsAccountException("이미 존재하는 아이디입니다.");
        }
        if(adminRepository.existsByName(request.getName())) {
            throw new AlreadyExistsNameException("이미 존재하는 이름입니다.");
        }

        Admin admin = adminRepository.save(
                Admin.builder()
                        .accountId(request.getAccountId())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .name(request.getName())
                        .role(Role.USER)
                        .block(Block.ISACTIVE)
                        .refreshToken(null)
                        .build()
        );

        return mapAdminToSignUpResponse(admin);
    }

    @Transactional
    public SignInResponse signIn(SignInRequest request) {
        Admin admin = adminRepository.findByAccountId(request.getAccountId())
                .orElseThrow(() -> new NotFoundAdminException("해당하는 아이디가 없습니다."));

        if(!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }

        TokenInfo accessToken = tokenProvider.createAccessToken(admin.getAccountId(), admin.getRole());
        TokenInfo refreshToken = tokenProvider.createRefreshToken(admin.getAccountId(), admin.getRole());
        admin.updateRefreshToken(refreshToken.getToken());
        return mapAdminToSignInResponse(admin, accessToken.getToken());
    }

    @Transactional
    public void logOut(String accountId) {
        Admin admin = adminRepository.findByAccountId(accountId)
                .orElseThrow(() -> new NotFoundAdminException("해당하는 아이디가 없습니다."));
        admin.updateRefreshToken(null);
    }

    @Transactional
    public ReissueResponse reissue(String accountId, String refreshToken) {
        log.info("Reissuing tokens for accountId: {}", accountId);

        Admin admin = adminRepository.findByAccountId(accountId)
                .orElseThrow(() -> new NotFoundAdminException("해당하는 아이디가 없습니다."));

        if(!admin.getRefreshToken().equals(refreshToken)) {
            throw new RefreshTokenNotFoundException("리프레쉬 토큰에서 유저정보를 찾을 수 없습니다.");
        }
        tokenProvider.validateToken(tokenProvider.resolveToken(refreshToken));

        TokenInfo newAccessToken = tokenProvider.createAccessToken(admin.getAccountId(), admin.getRole());
        TokenInfo newRefreshToken = tokenProvider.createRefreshToken(admin.getAccountId(), admin.getRole());

        admin.updateRefreshToken(newRefreshToken.getToken());

        return ReissueResponse.builder()
                .accessToken(newAccessToken.getToken())
                .refreshToken(newRefreshToken.getToken())
                .build();
    }

    public Boolean checkTokenValidation(String refreshToken) {
        String jwtToken = tokenProvider.resolveToken(refreshToken);
        if (jwtToken != null && tokenProvider.validateToken(jwtToken)) {
            log.info("토큰 유효성 검증 성공");
            return true;
        }
        else {
            throw new InvalidTokenException("토큰이 존재하지 않습니다.");
        }
    }



    private SignUpResponse mapAdminToSignUpResponse(Admin admin) {
        return SignUpResponse.builder()
                .id(admin.getAdminId())
                .accountId(admin.getAccountId())
                .name(admin.getName())
                .build();
    }
    private SignInResponse mapAdminToSignInResponse(Admin admin, String accessToken) {
        return SignInResponse.builder()
                .id(admin.getAdminId())
                .accountId(admin.getAccountId())
                .role(admin.getRole().toString())
                .accessToken(accessToken)
                .refreshToken(admin.getRefreshToken())
                .build();
    }
    private UpdateRoleResponse mapAdminToUpdateRoleResponse(Admin admin) {
        return UpdateRoleResponse.builder()
                .id(admin.getAdminId())
                .accountId(admin.getAccountId())
                .role(admin.getRole().toString())
                .build();
    }


}
