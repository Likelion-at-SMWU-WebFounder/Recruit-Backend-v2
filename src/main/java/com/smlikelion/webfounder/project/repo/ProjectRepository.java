package com.smlikelion.webfounder.project.repo;

import com.smlikelion.webfounder.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
