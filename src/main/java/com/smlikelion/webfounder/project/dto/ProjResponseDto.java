// 프로젝트 전체 조회할 때 응답할 값 명시
package com.smlikelion.webfounder.project.dto;

import com.smlikelion.webfounder.project.entity.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProjResponseDto {
    private Long projectId;
    private String title;
    private String no;
    private Integer year;
    private String summary;
    private String bgImg;

    public ProjResponseDto(Project project){
        this.projectId=project.getProjectId();
        this.title= project.getTitle();
        this.no=project.getYear()-2012+"기";
        this.year= project.getYear();
        this.summary= project.getSummary();
        this.bgImg=project.getBgImg();
    }
}
