package com.aicollab.backend.analysis.controller;

import com.aicollab.backend.analysis.dto.response.AnalysisRunResponse;
import com.aicollab.backend.analysis.service.AnalysisRunQueryService;
import com.aicollab.backend.auth.security.UserPrincipal;
import com.aicollab.backend.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analysis-runs/")
public class AnalysisRunController {

    private final AnalysisRunQueryService analysisRunQueryService;

    @GetMapping("/{runId}")
    public ApiResponse<AnalysisRunResponse> getAnalysisRun(
            @PathVariable Long runId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        AnalysisRunResponse response =
                analysisRunQueryService.getById(runId, principal.getId());

        return ApiResponse.success(response);
    }
}
