package com.smlikelion.webfounder.project.entity;

import com.smlikelion.webfounder.global.entity.DateEntity;
import com.smlikelion.webfounder.project.dto.ProjRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Table(name="project")
@Getter
@Setter
@NoArgsConstructor
public class Project extends DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "summary", nullable = false)
    private String summary;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "year", nullable = false)
    private Integer year; // 2022, 2023 ...

    @Column(name = "team_name", nullable = false)
    private String teamName;

    @Column(name = "team_member", nullable = false)
    private String teamMember;

    @Column(name = "service_intro")
    private String servIntro; // pdf download -> S3

    @Column(name = "github_FE_url")
    @ColumnDefault("Null")
    private String gitFeUrl;

    @Column(name = "github_BE_url")
    private String gitBeUrl;

    @Column(name = "service_launch")
    private String servLaunch; // 서비스 배포 주소

    @Column(name = "bg_img")
    private String bgImg; // 배경 사진 -> S3

    //requestDto 정보를 가져와서 entity 만들 때 사용
    public Project(ProjRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.summary= requestDto.getSummary();
        this.content = requestDto.getContent();
        this.year = requestDto.getYear();
        this.teamName= requestDto.getTeamName();
        this.teamMember= requestDto.getTeamMember();
        this.servIntro= requestDto.getServIntro();
        this.gitBeUrl= requestDto.getGitBeUrl();
        this.gitFeUrl= requestDto.getGitFeUrl();
        this.servLaunch= requestDto.getServLaunch();
        this.bgImg= requestDto.getBgImg();
    }
}
