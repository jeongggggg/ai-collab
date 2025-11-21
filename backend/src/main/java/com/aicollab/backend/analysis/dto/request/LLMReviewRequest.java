package com.aicollab.backend.analysis.dto.request;

import com.aicollab.backend.infrastructure.github.parser.DiffChange;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LLMReviewRequest {
    private String filename;
    private List<DiffChange> diff;

    public LLMReviewRequest(String filename, List<DiffChange> diff) {
        this.filename = filename;
        this.diff = diff;
    }
}
