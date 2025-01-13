package com.smlikelion.webfounder.manage.entity;
import com.smlikelion.webfounder.Recruit.Entity.Joiner;
import com.smlikelion.webfounder.global.entity.DateEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "candidate")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Candidate extends DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "candidate_id")
    private Long candidateId;

    @Column(name = "docs", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Docs docs;

    @Column(name = "interview", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Interview interview;

    @Column(name = "interview_time")
    private String interviewTime;

    @JoinColumn(name = "joiner_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Joiner joiner;

    // joiner service에서 entity 생성 시 cadidate도 자동 생성
    public Candidate(Joiner joiner, String docs, String interview) {
        this.joiner=joiner;
        this.docs= Docs.valueOf(docs);
        this.interview= Interview.valueOf(interview);

    }
}

