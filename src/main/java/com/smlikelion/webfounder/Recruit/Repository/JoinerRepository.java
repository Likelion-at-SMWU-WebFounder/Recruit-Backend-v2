package com.smlikelion.webfounder.Recruit.Repository;

import com.smlikelion.webfounder.Recruit.Entity.Joiner;
import com.smlikelion.webfounder.Recruit.Entity.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface JoinerRepository extends JpaRepository<Joiner, Long> {
    Joiner findByNameAndStudentId(String name, String studentId);
    Long countByTrack(Track track);
    Page<Joiner> findAllByTrackOrderByCreatedAtAsc(Track track, Pageable pageable);
    Page<Joiner> findAllByOrderByCreatedAt(Pageable pageable);

    boolean existsByStudentId(String studentId);


}