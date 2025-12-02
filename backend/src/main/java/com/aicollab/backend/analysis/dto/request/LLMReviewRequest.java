package com.aicollab.backend.analysis.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class LLMReviewRequest {
    private String filename;
    private List<String> diff;
    private String fullCode;
}
