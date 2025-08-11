package com.smlikelion.webfounder.manage.service;

import com.smlikelion.webfounder.Recruit.Entity.Joiner;
import com.smlikelion.webfounder.Recruit.Repository.JoinerRepository;
import com.smlikelion.webfounder.admin.entity.Role;
import com.smlikelion.webfounder.manage.dto.response.DeleteDocsResponse;
import com.smlikelion.webfounder.admin.exception.UnauthorizedRoleException;
import com.smlikelion.webfounder.manage.repository.CandidateRepository;
import com.smlikelion.webfounder.security.AuthInfo;
import java.util.ArrayList;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class SQLExecutionService {
    private final JdbcTemplate jdbcTemplate;
    private final CandidateRepository candidateRepository;
    private final JoinerRepository joinerRepository;

    /**
     * 선택한 지원서류(Joiner + 연결된 Candidate)만 삭제
     * - 1) 유효한 권한 확인
     * - 2) 실제 존재하는 joiner만 선별
     * - 3) Candidate → Joiner 순으로 배치 삭제
     * - 4) 요청/삭제/실패 반환
     */
    @Transactional
    public DeleteDocsResponse deleteDocsByJoinerIds(AuthInfo authInfo, List<Long> joinerIds) {
        // 권한 정책: SUPERUSER + MANAGER 허용 (필요시 SUPERUSER만 허용으로 바꾸세요)
        if (!hasValidRoles(authInfo, List.of(Role.SUPERUSER, Role.MANAGER))) {
            throw new UnauthorizedRoleException("접근 권한이 없습니다.");
        }

        if (joinerIds == null || joinerIds.isEmpty()) {
            return DeleteDocsResponse.builder()
                    .requested(0).deleted(0).failed(List.of())
                    .build();
        }

        // 중복 제거 + 순서 유지
        List<Long> requested = joinerIds.stream().distinct().collect(Collectors.toList());

        // 실제 존재하는 joiner 조회
        List<Long> existing = joinerRepository.findAllById(requested)
                .stream().map(Joiner::getId).collect(Collectors.toList());

        // 실패 ID(= 요청했지만 존재하지 않는 ID, 또는 나중에 삭제 실패한 ID)
        List<Long> failed = new ArrayList<>();
        // 존재하지 않는 것 먼저 추가
        failed.addAll(requested.stream()
                .filter(id -> !existing.contains(id))
                .collect(Collectors.toList()));

        if (existing.isEmpty()) {
            return DeleteDocsResponse.builder()
                    .requested(requested.size())
                    .deleted(0)
                    .failed(failed)
                    .build();
        }

        try {
            // 순서 중요: Candidate → Joiner
            candidateRepository.deleteByJoinerIds(existing);
            joinerRepository.deleteAllByIdInBatch(existing);
        } catch (DataAccessException ex) {
            // 배치 삭제 전체 실패 시, 개별 삭제로 폴백하여 최대한 진행
            log.warn("Batch delete failed, fallback to per-id deletion. cause={}", ex.getMessage());

            int success = 0;
            for (Long id : existing) {
                try {
                    candidateRepository.deleteByJoinerIds(List.of(id));
                    joinerRepository.deleteAllByIdInBatch(List.of(id));
                    success++;
                } catch (Exception e) {
                    log.error("Failed to delete joinerId {}: {}", id, e.getMessage());
                    failed.add(id);
                }
            }

            return DeleteDocsResponse.builder()
                    .requested(requested.size())
                    .deleted(success)
                    .failed(failed)
                    .build();
        }

        // 배치 성공 시: existing 전부 삭제됨
        return DeleteDocsResponse.builder()
                .requested(requested.size())
                .deleted(existing.size())
                .failed(failed) // 존재하지 않던 ID만 포함
                .build();
    }


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
