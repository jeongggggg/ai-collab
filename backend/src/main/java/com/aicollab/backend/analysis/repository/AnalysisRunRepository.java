package com.aicollab.backend.analysis.repository;

import com.aicollab.backend.analysis.domain.AnalysisRun;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnalysisRunRepository extends JpaRepository<AnalysisRun, Long> {
    List<AnalysisRun> findByUploadId(Long uploadId);
}
