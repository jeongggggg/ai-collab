package com.aicollab.backend.analysis.service;

import com.aicollab.backend.analysis.domain.AnalysisRun;
import com.aicollab.backend.analysis.dto.response.AnalysisRunResponse;
import com.aicollab.backend.analysis.repository.AnalysisRunRepository;
import com.aicollab.backend.upload.domain.Upload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalysisRunQueryService {

    private final AnalysisRunRepository analysisRunRepository;

    public AnalysisRunResponse getById(Long runId, Long requesterId) {

        AnalysisRun run = analysisRunRepository.findById(runId)
                .orElseThrow(() -> new IllegalArgumentException("AnalysisRun not found"));

        Upload upload = run.getUpload();

        // 권한 검증 (업로드 owner만 조회 가능)
        if (!upload.getProject().getOwner().getId().equals(requesterId)) {
            throw new IllegalArgumentException("Not allowed to view this analysis run");
        }

        return AnalysisRunResponse.from(run);
    }
}