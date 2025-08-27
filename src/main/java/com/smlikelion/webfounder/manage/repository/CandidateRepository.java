package com.smlikelion.webfounder.manage.repository;

import com.smlikelion.webfounder.Recruit.Entity.Joiner;
import com.smlikelion.webfounder.manage.entity.Candidate;
import com.smlikelion.webfounder.manage.entity.Docs;
import com.smlikelion.webfounder.manage.entity.Interview;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    Optional<Candidate> findByJoiner(Joiner joiner);
    List<Candidate> findAllByDocs(Docs docs);
    List<Candidate> findAllByInterview(Interview interview);
    List<Candidate> findAllByDocsAndInterview(Docs docs, Interview interview);
    Candidate findByJoinerAndDocs(Joiner joiner, Docs docs);
    Candidate findByJoinerAndInterview(Joiner joiner, Interview interview);
    @Query("SELECT j, c FROM Joiner j JOIN Candidate c ON j = c.joiner WHERE c.docs = ?1")
    List<Object[]> findAllJoinerAndCandidateByDocs(Docs docs);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from Candidate c where c.joiner.id in :joinerIds") // Joiner의 필드명이 id여야 함(= getId)
    int deleteByJoinerIds(@Param("joinerIds") Collection<Long> joinerIds);
}
