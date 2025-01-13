// 프로젝트 상세 조회, 수정할 때 응답할 값 명시
package com.smlikelion.webfounder.project.dto;

import com.smlikelion.webfounder.project.entity.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProjListResponseDto {
    private Long projectId;
    private String title;
    private String summary;
    private String content;
    private Integer year;
    private String teamName;
    private String teamMember;
    private String servIntro;
    private String gitBeUrl;
    private String gitFeUrl;
    private String servLaunch;
    private String bgImg;

    public ProjListResponseDto(Project project){
        this.projectId=project.getProjectId();
        this.title= project.getTitle();
        this.summary= project.getSummary();
        this.content= project.getContent();
        this.year=project.getYear();
        this.teamName= project.getTeamName();
        this.teamMember= project.getTeamMember();
        this.servIntro= project.getServIntro();
        this.gitBeUrl= project.getGitBeUrl();
        this.gitFeUrl= project.getGitFeUrl();
        this.servLaunch= project.getServLaunch();
        this.bgImg= project.getBgImg();
    }
}
