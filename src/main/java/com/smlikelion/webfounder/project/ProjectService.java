package com.smlikelion.webfounder.project;

import com.smlikelion.webfounder.project.dto.ProjListResponseDto;
import com.smlikelion.webfounder.project.dto.ProjRequestDto;
import com.smlikelion.webfounder.project.dto.ProjResponseDto;
import com.smlikelion.webfounder.project.entity.Project;
import com.smlikelion.webfounder.project.exception.NotfoundProjException;
import com.smlikelion.webfounder.project.repo.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    // [POST] 프로젝트 작성
    public ProjRequestDto createProj(ProjRequestDto requestDto) {
        try {
            Project project = new Project(requestDto);
            projectRepository.save(project);
            return new ProjRequestDto(project);
        }catch (Exception e){
            throw new NotfoundProjException("프로젝트 작성 실패");

        }
    }

    // [GET] 프로젝트 전체 조회
    public List<ProjResponseDto> findAllProj(){
        try{
            List<Project> projectList=projectRepository.findAll();
            List<ProjResponseDto> responseDtoList=new ArrayList<>();

            for(Project project:projectList){
                responseDtoList.add(new ProjResponseDto(project));
            }
            return responseDtoList;
        }catch (Exception e){
            throw new NotfoundProjException("프로젝트 전체 조회 실패");
        }
    }

    // [GET] 프로젝트 상세 조회
    public ProjListResponseDto findOneProj(Long id){
        Project project=projectRepository.findById(id).orElseThrow(
                ()-> new NotfoundProjException("프로젝트 상세 조회 실패")
        );
        return new ProjListResponseDto(project);
    }

    // [DELETE] 프로젝트 삭제
    public Long delProj(Long id){
        try{
            projectRepository.deleteById(id);
            return id;
        }catch (Exception e){
            throw new NotfoundProjException("프로젝트 삭제 실패");
        }
    }

    // [update] 프로젝트 수정
    @Transactional
    public ProjListResponseDto updateProj(Long id, ProjRequestDto requestDto) {
        Project project = projectRepository.findById(id).orElseThrow(
                ()-> new NotfoundProjException("프로젝트 수정 실패")
        );

        try{
            project.setTitle(requestDto.getTitle());
            project.setSummary(requestDto.getSummary());
            project.setContent(requestDto.getContent());
            project.setYear(requestDto.getYear());
            project.setTeamName(requestDto.getTeamName());
            project.setTeamMember(requestDto.getTeamMember());
            project.setServIntro(requestDto.getServIntro());
            project.setGitBeUrl(requestDto.getGitBeUrl());
            project.setGitFeUrl(requestDto.getGitFeUrl());
            project.setServLaunch(requestDto.getServLaunch());
            project.setBgImg(requestDto.getBgImg());
            return new ProjListResponseDto(project);
        }catch (Exception e){
            throw new NotfoundProjException("프로젝트 수정 실패");
        }

    }
}
