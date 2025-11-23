package com.aicollab.backend.github.service;

import com.aicollab.backend.analysis.dto.request.LLMReviewRequest;
import com.aicollab.backend.analysis.service.LLMReviewService;
import com.aicollab.backend.github.dto.response.PrAnalysisResponse;
import com.aicollab.backend.infrastructure.github.dto.response.PullRequestFileResponse;
import com.aicollab.backend.infrastructure.github.parser.DiffParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrAnalysisService {

    private final GithubService githubService;
    private final DiffParser diffParser;
    private final LLMReviewService llmReviewService;

    public PrAnalysisResponse analyze(String owner, String repo, int prNumber) {

        List<PullRequestFileResponse> files = githubService.getPullRequestFiles(owner, repo, prNumber);
        List<PrAnalysisResponse.FileAnalysis> analysisList = new ArrayList<>();

        for (PullRequestFileResponse file : files) {

            // diff 없는 경우 스킵
            if (file.getPatch() == null) continue;

            var parsed = diffParser.parse(file.getPatch());

            // diff → string 변환
            List<String> diffLines = parsed.stream()
                    .map(dc -> switch (dc.getType()) {
                        case "added" -> "+" + dc.getContent();
                        case "removed" -> "-" + dc.getContent();
                        default -> dc.getContent();
                    })
                    .toList();

            // 파일 전체 조회
            var content = githubService.getFileContent(owner, repo, file.getFilename(), file.getSha());

            String fullCode = "";

            if (content.getEncodedContent() != null) {
                fullCode = new String(Base64.getDecoder().decode(content.getEncodedContent()));
            }

            // GPT 요청 DTO
            LLMReviewRequest request = LLMReviewRequest.builder()
                    .filename(file.getFilename())
                    .diff(diffLines)
                    .fullCode(fullCode)
                    .build();

            String review = llmReviewService.generateReview(request).getReview();

            analysisList.add(
                    PrAnalysisResponse.FileAnalysis.builder()
                            .filename(file.getFilename())
                            .review(review)
                            .build()
            );
        }
        return PrAnalysisResponse.of(prNumber, analysisList);
    }
}