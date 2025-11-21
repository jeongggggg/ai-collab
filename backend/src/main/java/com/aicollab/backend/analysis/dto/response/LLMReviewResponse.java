package com.aicollab.backend.analysis.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LLMReviewResponse {
    private String review; // LLM이 생성한 리뷰 메세지
}
