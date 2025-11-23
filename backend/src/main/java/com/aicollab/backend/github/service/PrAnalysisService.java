package com.aicollab.backend.github.service;

import com.aicollab.backend.analysis.dto.request.LLMReviewRequest;
import com.aicollab.backend.analysis.service.LLMReviewService;
import com.aicollab.backend.github.dto.response.PrAnalysisResponse;
import com.aicollab.backend.infrastructure.github.dto.response.GithubFileContentResponse;
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

            // fullCode 조회 (patch 없어도 전체 파일 로딩 가능)
            var content = githubService.getFileContent(owner, repo, file.getFilename(), extractSha(file));
            String fullCode = decodeContent(content);

            // DIFF 처리
            List<String> diffLines = null;

            if (file.getPatch() != null) {
                var parsed = diffParser.parse(file.getPatch());
                diffLines = parsed.stream()
                        .map(dc -> switch (dc.getType()) {
                            case "added" -> "+" + dc.getContent();
                            case "removed" -> "-" + dc.getContent();
                            default -> dc.getContent();
                        })
                        .toList();
            }

            // Diff도 없고 fullCode도 없으면 스킵
            if ((diffLines == null || diffLines.isEmpty()) && fullCode.isBlank()) {
                continue;
            }

            // GPT 요청 DTO 생성
            LLMReviewRequest request = LLMReviewRequest.builder()
                    .filename(file.getFilename())
                    .diff(diffLines)       // patch 없는 경우 null
                    .fullCode(fullCode)     // fullCode 기반 리뷰 가능
                    .build();

            // 리뷰 생성
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

    // SHA 추출
    private String extractSha(PullRequestFileResponse file) {
        try {
            return file.getRawUrl().split("/")[file.getRawUrl().split("/").length - 2];
        } catch (Exception e) {
            return null;
        }
    }

    // BASE64 디코딩
    private String decodeContent(GithubFileContentResponse content) {
        try {
            if (content != null && content.getEncodedContent() != null) {
                return new String(Base64.getDecoder().decode(content.getEncodedContent()));
            }
        } catch (Exception ignored) {}
        return "";
    }
}