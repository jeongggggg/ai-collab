package com.aicollab.backend.upload.service;

import com.aicollab.backend.project.domain.Project;
import com.aicollab.backend.project.repository.ProjectRepository;
import com.aicollab.backend.upload.domain.Upload;
import com.aicollab.backend.upload.domain.UploadStatus;
import com.aicollab.backend.upload.dto.request.UploadCreateRequest;
import com.aicollab.backend.upload.repository.UploadRepository;
import com.aicollab.backend.user.domain.User;
import com.aicollab.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UploadService {

    private final ProjectRepository projectRepository;
    private final UploadRepository uploadRepository;
    private final UserRepository userRepository;

    // 생성
    public Upload create(Long projectId, Long userId, UploadCreateRequest req) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!project.getOwner().getId().equals(owner.getId())) {
            throw new IllegalArgumentException("User is not owner of this project");
        }

        Upload upload = Upload.builder()
                .project(project)
                .commitSha(req.getCommitSha())
                .status(UploadStatus.PENDING)
                .build();

        return uploadRepository.save(upload);
    }

    // 조회
    public Upload getById(Long projectId, Long uploadId, Long userId) {

        Upload upload = uploadRepository.findById(uploadId)
                .orElseThrow(() -> new IllegalArgumentException("Upload not found"));
        if(!upload.getProject().getId().equals(projectId)) {
            throw new IllegalArgumentException("Project is not owner of this upload");
        }
        return upload;
    }
}
