package com.smlikelion.webfounder.manage.entity;
import com.smlikelion.webfounder.Recruit.Entity.Track;
import com.smlikelion.webfounder.global.entity.DateEntity;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "question")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Question extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long questionId;

    @Column(name = "year")
    @NotNull
    private Long year;

    @Column(name = "track")
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private Track track;

    @Column(name = "number")
    @NotNull
    private Long number;

    @Column(name = "content")
    @NotNull
    private String content;

    @Column(name = "max_length")
    @NotNull
    private Long maxLength;

}
