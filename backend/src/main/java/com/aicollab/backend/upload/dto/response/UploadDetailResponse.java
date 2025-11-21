package com.aicollab.backend.upload.dto.response;

import com.aicollab.backend.analysis.domain.AnalysisRun;
import com.aicollab.backend.analysis.dto.response.AnalysisRunResponse;
import com.aicollab.backend.upload.domain.Upload;
import com.aicollab.backend.upload.domain.UploadStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class UploadDetailResponse {

    private Long id;
    private String commitSha;
    private UploadStatus status;
    private LocalDateTime createdAt;
    private List<AnalysisRunResponse> analysisRuns;

    public static UploadDetailResponse from(Upload upload, List<AnalysisRun> runs) {
        return UploadDetailResponse.builder()
                .id(upload.getId())
                .commitSha(upload.getCommitSha())
                .status(upload.getStatus())
                .createdAt(upload.getCreatedAt())
                .analysisRuns(
                        runs.stream()
                                .map(AnalysisRunResponse::from)
                                .collect(Collectors.toList())
                )
                .build();
    }
}