package com.aicollab.backend.github.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PrAnalysisResponse {

    private int prNumber;
    private List<FileAnalysis> files;

    @Getter
    @Builder
    public static class FileAnalysis {
        private String filename;
        private String review;
    }

    public static PrAnalysisResponse of(int prNumber, List<FileAnalysis> files) {
        return PrAnalysisResponse.builder()
                .prNumber(prNumber)
                .files(files)
                .build();
    }
}
