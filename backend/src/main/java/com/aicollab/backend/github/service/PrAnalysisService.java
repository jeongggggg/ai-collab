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

        // 파일별 리뷰 텍스트 누적 저장
        List<String> accumulatedReviews = new ArrayList<>();

        for (PullRequestFileResponse file : files) {

            // 1) SHA 추출
            String sha = extractSha(file);

            // 2) 원본 코드 조회
            String fullCode = fetchFullCode(owner, repo, file.getFilename(), sha);

            // 3) DIFF 처리
            List<String> diffLines = buildDiffLines(file);

            // Diff도 fullCode도 없으면 리뷰 불가 → 건너뜀
            if ((diffLines == null || diffLines.isEmpty()) && fullCode.isBlank()) {
                continue;
            }

            // 4) GPT 요청 DTO 생성
            LLMReviewRequest request = LLMReviewRequest.builder()
                    .filename(file.getFilename())
                    .diff(diffLines == null ? List.of() : diffLines)
                    .fullCode(fullCode)
                    .build();

            // 5) 리뷰 생성
            String review = llmReviewService.generateReview(request).getReview();

            accumulatedReviews.add("### [" + file.getFilename() + "]\n" + review);

            analysisList.add(
                    PrAnalysisResponse.FileAnalysis.builder()
                            .filename(file.getFilename())
                            .review(review)
                            .build()
            );
        }

        String summaryReview = llmReviewService.generateSummary(accumulatedReviews);

        return PrAnalysisResponse.of(prNumber, summaryReview, analysisList);
    }

    // SHA 추출
    private String extractSha(PullRequestFileResponse file) {
        try {
            String[] parts = file.getRawUrl().split("/");
            return parts[5];   // SHA 위치
        } catch (Exception e) {
            return null;
        }
    }

    // BASE64 디코딩
    private String fetchFullCode(String owner, String repo, String filename, String sha) {
        try {
            GithubFileContentResponse content =
                    githubService.getFileContent(owner, repo, filename, sha);

            if (content != null && content.getEncodedContent() != null) {
                return new String(Base64.getMimeDecoder().decode(content.getEncodedContent()));
            }
        } catch (Exception ignored) {}

        return "";
    }

    // Diff 문자열 리스트 변환
    private List<String> buildDiffLines(PullRequestFileResponse file) {
        if (file.getPatch() == null) return null;

        var parsed = diffParser.parse(file.getPatch());

        return parsed.stream()
                .map(dc -> switch (dc.getType()) {
                    case "added" -> "+" + dc.getContent();
                    case "removed" -> "-" + dc.getContent();
                    default -> dc.getContent();
                })
                .toList();
    }
}