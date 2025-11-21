package com.aicollab.backend.analysis.orchestrator;

import com.aicollab.backend.analysis.dto.request.LLMReviewRequest;
import com.aicollab.backend.analysis.dto.response.LLMReviewResponse;
import com.aicollab.backend.analysis.service.LLMReviewService;
import com.aicollab.backend.infrastructure.github.dto.response.PullRequestFileResponse;
import com.aicollab.backend.infrastructure.github.parser.DiffParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrReviewOrchestrator {

    private final DiffParser diffParser;
    private final LLMReviewService llmReviewService;

    public LLMReviewResponse analyze(PullRequestFileResponse file) {

        // patch -> DiffChange 목록 파싱
        var parsedLines = diffParser.parse(file.getPatch());

        // GPT 입력  DTO
        LLMReviewRequest request = LLMReviewRequest.builder()
                .filename(file.getFilename())
                .diff(parsedLines)
                .build();

        // LLM 리뷰 생성
        return llmReviewService.generateReview(request);
    }
}
