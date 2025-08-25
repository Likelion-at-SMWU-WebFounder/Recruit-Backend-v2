package com.smlikelion.webfounder.manage.service;

import com.smlikelion.webfounder.Recruit.Entity.Joiner;
import com.smlikelion.webfounder.Recruit.Repository.JoinerRepository;
import com.smlikelion.webfounder.admin.entity.Role;
import com.smlikelion.webfounder.manage.dto.response.DeleteDocsResponse;
import com.smlikelion.webfounder.admin.exception.UnauthorizedRoleException;
import com.smlikelion.webfounder.manage.repository.CandidateRepository;
import com.smlikelion.webfounder.security.AuthInfo;
import java.util.Collections;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Transactional
    public DeleteDocsResponse deleteDocsByJoinerIds(AuthInfo authInfo, List<Long> joinerIds) {
        if (!hasValidRoles(authInfo, List.of(Role.SUPERUSER, Role.MANAGER))) {
            throw new UnauthorizedRoleException("접근 권한이 없습니다.");
        }

        if (joinerIds == null || joinerIds.isEmpty()) {
            return DeleteDocsResponse.builder()
                    .requested(0).deleted(0).failed(List.of())
                    .build();
        }

        // 1) 중복 제거 + 순서 유지
        List<Long> requested = joinerIds.stream().distinct().collect(Collectors.toList());

        // 2) 실제 존재하는 joiner 조회
        List<Long> existing = joinerRepository.findAllById(requested)
                .stream().map(Joiner::getId).collect(Collectors.toList());

        // 3) 존재하지 않는 ID를 실패 목록에 선반영
        List<Long> failed = requested.stream()
                .filter(id -> !existing.contains(id))
                .collect(Collectors.toList());

        if (existing.isEmpty()) {
            return DeleteDocsResponse.builder()
                    .requested(requested.size())
                    .deleted(0)
                    .failed(failed)
                    .build();
        }

        // 4) 배치 삭제 시도 (Candidate → Joiner)
        try {
            candidateRepository.deleteByJoinerIds(existing);
            joinerRepository.deleteAllByIdInBatch(existing);

            return DeleteDocsResponse.builder()
                    .requested(requested.size())
                    .deleted(existing.size())
                    .failed(failed) // 없는 ID만 포함
                    .build();

        } catch (RuntimeException ex) {
            log.warn("Batch delete failed. Fallback to per-id. cause={}", ex.getMessage());

            int success = fallbackDeletePerId(existing, failed);
            return DeleteDocsResponse.builder()
                    .requested(requested.size())
                    .deleted(success)
                    .failed(failed) // 없는 ID + per-id에서도 실패한 ID
                    .build();
        }
    }

    /** 개별 삭제 폴백: 가능한 만큼 삭제하고, 실패 ID를 failed에 추가 */
    private int fallbackDeletePerId(List<Long> existing, List<Long> failed) {
        int success = 0;
        for (Long id : existing) {
            try {
                // Java 8 호환: Collections.singletonList 사용
                candidateRepository.deleteByJoinerIds(Collections.singletonList(id));
                joinerRepository.deleteAllByIdInBatch(Collections.singletonList(id));
                success++;
            } catch (Exception e) {
                log.error("Failed to delete joinerId {}: {}", id, e.getMessage());
                failed.add(id);
            }
        }
        return success;
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
