package com.aicollab.backend.upload.repository;

import com.aicollab.backend.upload.domain.Upload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadRepository extends JpaRepository<Upload, Long> {
    long countByProjectId(Long projectId);
}
