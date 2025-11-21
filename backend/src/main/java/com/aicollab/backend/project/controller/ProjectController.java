package com.aicollab.backend.project.controller;

import com.aicollab.backend.auth.security.UserPrincipal;
import com.aicollab.backend.global.response.ApiResponse;
import com.aicollab.backend.project.domain.Project;
import com.aicollab.backend.project.dto.request.ProjectCreateRequest;
import com.aicollab.backend.project.dto.response.ProjectResponse;
import com.aicollab.backend.project.service.ProjectService;
import com.aicollab.backend.user.domain.User;
import com.aicollab.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final UserRepository userRepository;

    // 프로젝트 생성 (로그인 사용자 + 프로젝트 소유자로 설정)
    @PostMapping
    public ApiResponse<Project> create(
            @RequestBody ProjectCreateRequest req,
            @AuthenticationPrincipal UserPrincipal principal
    ) {

        // principal.id -> DB User 조회
        User owner = userRepository.findById(principal.getId())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        return ApiResponse.success(projectService.create(req,owner));
    }

    // 내가 가진 프로젝트 목록 조회
    @GetMapping
    public ApiResponse<List<Project>> getMyProjects(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        return ApiResponse.success(projectService.getMyProjects(user));
    }

    // 프로젝트 상세 조회
    @GetMapping("/{id}")
    public ApiResponse<ProjectResponse> getProject(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ApiResponse.success(
                projectService.toResponse(projectService.get(id))
        );
    }

    // 프로젝트 삭제 (Owner만 가능)
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        projectService.delete(id,user);
        return ApiResponse.success(null);
    }
}
