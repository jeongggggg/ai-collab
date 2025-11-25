package com.aicollab.backend.project.service;

import com.aicollab.backend.project.domain.Project;
import com.aicollab.backend.project.dto.request.ProjectCreateRequest;
import com.aicollab.backend.project.dto.response.ProjectResponse;
import com.aicollab.backend.project.repository.ProjectRepository;
import com.aicollab.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    // 프로젝트 생성
    public Project create(ProjectCreateRequest req, User owner) {
        Project project = Project.builder()
                .name(req.getName())
                .description(req.getDescription())
                .owner(owner)
                .repoOwner(req.getRepoOwner())
                .repoName(req.getRepoName())
                .build();

        return projectRepository.save(project);
    }

    // 로그인한 사용자의 프로젝트 목록 조회
    public List<Project> getMyProjects(User user) {
        return projectRepository.findAll()
                .stream()
                .filter(p -> p.getOwner().getId().equals(user.getId()))
                .toList();
    }

    // 프로젝트 상세 조회 (단건)
    public Project get(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
    }

    // Project 엔티티 -> 응답 DTO 변환
    public ProjectResponse toResponse(Project p) {
        return ProjectResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .repoOwner(p.getRepoOwner())
                .repoName(p.getRepoName())
                .owner(
                        ProjectResponse.OwnerInfo.builder()
                                .id(p.getOwner().getId())
                                .login(p.getOwner().getLogin())
                                .build()
                )
                .build();
    }

    // 프로젝트 삭제 (Owner만 가능)
    public void delete(Long id, User requester) {
        Project project = get(id);

        if(!project.getOwner().getId().equals(requester.getId())) {
            throw new IllegalArgumentException("Only the owner can delete this project");
        }

        projectRepository.delete(project);
    }
}
