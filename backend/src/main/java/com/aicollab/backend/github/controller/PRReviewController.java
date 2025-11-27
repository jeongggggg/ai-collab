package com.aicollab.backend.github.controller;

import com.aicollab.backend.auth.security.UserPrincipal;
import com.aicollab.backend.global.response.ApiResponse;
import com.aicollab.backend.github.dto.response.PrAnalysisResponse;
import com.aicollab.backend.github.service.PrAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/github")
public class PRReviewController {

    private final PrAnalysisService prAnalysisService;

    @PostMapping("/prs/{projectId}/{prNumber}/review")
    public ApiResponse<PrAnalysisResponse> createPrReview(
            @PathVariable Long projectId,
            @PathVariable int prNumber,
            @RequestParam String owner,
            @RequestParam String repo,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        // LLM 분석 실행
        PrAnalysisResponse result = prAnalysisService.analyze(
                owner,
                repo,
                prNumber
        );
        return ApiResponse.success(result);
    }
}