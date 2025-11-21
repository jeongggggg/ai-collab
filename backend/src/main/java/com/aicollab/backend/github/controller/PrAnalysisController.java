package com.aicollab.backend.github.controller;

import com.aicollab.backend.github.dto.response.PrAnalysisResponse;
import com.aicollab.backend.github.service.PrAnalysisService;
import com.aicollab.backend.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/github/prs")
public class PrAnalysisController {

    private final PrAnalysisService prAnalysisService;

    @GetMapping("/{prNumber}/analysis")
    public ApiResponse<PrAnalysisResponse> analyze(
            @PathVariable int prNumber,
            @RequestParam String owner,
            @RequestParam String repo
    ) {
        return ApiResponse.success(prAnalysisService.analyze(owner, repo, prNumber));
    }
}
