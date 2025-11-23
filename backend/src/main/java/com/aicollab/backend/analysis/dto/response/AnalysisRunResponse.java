package com.aicollab.backend.analysis.dto.response;

import com.aicollab.backend.analysis.domain.AnalysisRun;
import com.aicollab.backend.analysis.domain.AnalysisStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnalysisRunResponse {

    private Long id;
    private AnalysisStatus status;
    private String review;
    private String summaryReview;

    public static AnalysisRunResponse from(AnalysisRun run) {
        return AnalysisRunResponse.builder()
                .id(run.getId())
                .status(run.getStatus())
                .review(run.getReview())
                .summaryReview(run.getSummaryReview())
                .build();
    }
}