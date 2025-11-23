package com.aicollab.backend.github.service;

import com.aicollab.backend.analysis.dto.request.LLMReviewRequest;
import com.aicollab.backend.analysis.service.LLMReviewService;
import com.aicollab.backend.github.dto.response.PrAnalysisResponse;
import com.aicollab.backend.infrastructure.github.dto.response.PullRequestFileResponse;
import com.aicollab.backend.infrastructure.github.parser.DiffParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrAnalysisService {

    private final GithubService githubService;
    private final DiffParser diffParser;
    private final LLMReviewService llmReviewService;

    public PrAnalysisResponse analyze(String owner, String repo, int prNumber) {

        // 1) PR 파일 목록 가져오기
        List<PullRequestFileResponse> files = githubService.getPullRequestFiles(owner, repo, prNumber);
        List<PrAnalysisResponse.FileAnalysis> analysisList = new ArrayList<>();

        for(PullRequestFileResponse file : files) {

            // patch 없는 파일 스킵
            if (file.getPatch() == null) continue;

            // 2) diff 파싱
            var parsed = diffParser.parse(file.getPatch());

            // DiffChange → String 변환
            List<String> diffLines = parsed.stream()
                    .map(dc -> switch (dc.getType()) {
                        case "added" -> "+" + dc.getContent();
                        case "removed" -> "-" + dc.getContent();
                        default -> dc.getContent();
                    })
                    .toList();

            // LLM 요청 DTO
            LLMReviewRequest request = LLMReviewRequest.builder()
                    .filename(file.getFilename())
                    .diff(diffLines)
                    .build();

            // 3) OpenAI 리뷰 생성
            String review = llmReviewService.generateReview(request).getReview();

            // 4) 파일별 분석 저장
            analysisList.add(
                    PrAnalysisResponse.FileAnalysis.builder()
                            .filename(file.getFilename())
                            .review(review)
                            .build()
            );
        }
        // 5) 최종 응답 생성
        return PrAnalysisResponse.of(prNumber, analysisList);
    }
}
