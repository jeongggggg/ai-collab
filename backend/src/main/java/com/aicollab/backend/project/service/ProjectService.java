package com.aicollab.backend.project.service;

import com.aicollab.backend.project.domain.Project;
import com.aicollab.backend.project.dto.request.ProjectCreateRequest;
import com.aicollab.backend.project.repository.ProjectRepository;
import com.aicollab.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public Project create(ProjectCreateRequest req, User owner) {
        Project project = Project.builder()
                .name(req.getName())
                .description(req.getDescription())
                .owner(owner)
                .build();

        return projectRepository.save(project);
    }

    public List<Project> getMyProjects(User user) {
        return projectRepository.findAll()
                .stream()
                .filter(p -> p.getOwner().getId().equals(user.getId()))
                .toList();
    }

    public Project get(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
    }

    public void delete(Long id, User requester) {
        Project project = get(id);

        if(!project.getOwner().getId().equals(requester.getId())) {
            throw new IllegalArgumentException("Only the owner can delete this project");
        }

        projectRepository.delete(project);
    }
}
