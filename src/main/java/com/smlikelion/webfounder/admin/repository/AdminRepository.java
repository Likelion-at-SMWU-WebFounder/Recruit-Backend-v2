package com.smlikelion.webfounder.admin.repository;

import com.smlikelion.webfounder.admin.entity.Admin;
import com.smlikelion.webfounder.admin.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository <Admin, Long> {
    boolean existsByRole(Role role);
    boolean existsByAccountId(String accountId);
    boolean existsByName(String name);

    Optional<Admin> findByAdminIdAndAccountId(Long adminId, String accountId);
    Optional<Admin> findByAccountId(String accountId);

}
