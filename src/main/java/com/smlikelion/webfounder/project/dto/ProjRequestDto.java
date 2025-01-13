// 프로젝트 게시물 작성할 때 프론트한테 요청받을 값 명시
// 프로젝트 게시물 작성 시 응답 데이터 DTO로 재활용 함
package com.smlikelion.webfounder.project.dto;

import com.smlikelion.webfounder.project.entity.Project;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProjRequestDto {
    @NotNull(message = "프로젝트 제목을 입력해야 합니다.")
    private String title;
    @NotNull(message = "프로젝트 소개를 입력해야 합니다.")
    private String summary;
    @NotNull(message = "프로젝트 내용을 입력해야 합니다.")
    private String content;
    @NotNull(message = "프로젝트 개발 연도를 입력해야 합니다.")
    private Integer year;
    @NotNull(message = "팀 이름을 입력해야 합니다.")
    private String teamName;
    @NotNull(message = "팀원 이름을 입력해야 합니다.")
    private String teamMember;
    @NotNull(message = "서비스 소개를 입력해야 합니다.")
    private String servIntro; //nullable
    @NotNull(message = "깃허브 (백엔드) 주소를 입력해야 합니다.")
    private String gitBeUrl; //nullable
    private String gitFeUrl; //nullable
    @NotNull(message = "배포 링크를 입력해야 합니다.")
    private String servLaunch; //nullable
    @NotNull(message = "배경 이미지 S3 링크를 입력해야 합니다.")
    private String bgImg; //nullable

    public ProjRequestDto(Project project) {
        this.title= project.getTitle();
        this.summary= project.getSummary();
        this.content= project.getContent();
        this.year= project.getYear();
        this.teamName= project.getTeamName();
        this.teamMember= project.getTeamMember();
        this.servIntro= project.getServIntro();
        this.gitBeUrl= project.getGitBeUrl();
        this.gitFeUrl= project.getGitFeUrl();
        this.servLaunch= project.getServLaunch();
        this.bgImg= project.getBgImg();
    }
}
