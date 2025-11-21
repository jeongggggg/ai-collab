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

    @PostMapping
    public ApiResponse<Project> create(
            @RequestBody ProjectCreateRequest req,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        User owner = userRepository.findById(principal.getId())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        return ApiResponse.success(projectService.create(req,owner));
    }

    @GetMapping
    public ApiResponse<List<Project>> getMyProjects(
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        return ApiResponse.success(projectService.getMyProjects(user));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProjectResponse> getProject(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return ApiResponse.success(
                projectService.toResponse(projectService.get(id))
        );
    }

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
