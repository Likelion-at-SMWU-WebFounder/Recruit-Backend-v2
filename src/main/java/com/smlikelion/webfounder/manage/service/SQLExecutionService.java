package com.smlikelion.webfounder.manage.service;

import com.smlikelion.webfounder.Recruit.Repository.JoinerRepository;
import com.smlikelion.webfounder.admin.entity.Role;
import com.smlikelion.webfounder.global.dto.response.BaseResponse;
import com.smlikelion.webfounder.global.dto.response.ErrorCode;
import com.smlikelion.webfounder.manage.exception.DeleteEntityException;
import com.smlikelion.webfounder.admin.exception.UnauthorizedRoleException;
import com.smlikelion.webfounder.manage.repository.CandidateRepository;
import com.smlikelion.webfounder.security.AuthInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class SQLExecutionService {
    private final JdbcTemplate jdbcTemplate;
    private final CandidateRepository candidateRepository;
    private final JoinerRepository joinerRepository;

    public String deleteAllDocs(AuthInfo authInfo) {
        if(!hasValidRoles(authInfo, List.of(Role.SUPERUSER))) {
            throw new UnauthorizedRoleException("접근 권한이 없습니다.");
        }

        try {
            candidateRepository.deleteAll();
            joinerRepository.deleteAll();
            resetAutoIncrement("candidate");
            resetAutoIncrement("joiner");
        } catch (Exception e) {
            return "서류 합격자 전체 삭제 실패";
        }

        return "서류 합격자 전체 삭제 완료";
    }

    @Transactional
    public void executeSql(String sql) {
        jdbcTemplate.execute(sql);
    }

    private void resetAutoIncrement(String tableName) {
        executeSql("ALTER TABLE " + tableName + " AUTO_INCREMENT = 1");
    }
    private boolean hasValidRoles(AuthInfo authInfo, List<Role> allowedRoles) {
        return authInfo.getRoles().stream().anyMatch(allowedRoles::contains);
    }

}
