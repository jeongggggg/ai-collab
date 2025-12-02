package com.aicollab.backend.analysis.processor;

import com.aicollab.backend.analysis.dto.request.LLMReviewRequest;
import com.aicollab.backend.analysis.dto.response.LLMReviewResponse;
import com.aicollab.backend.infrastructure.openai.OpenAIClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LLMReviewProcessor {

    private final OpenAIClient openAIClient;

    public LLMReviewResponse generateReview(LLMReviewRequest req) {

        String prompt = buildPrompt(req);
        String reviewText = openAIClient.createChatCompletion(prompt);

        return new LLMReviewResponse(reviewText);
    }

    public String generateSummary(List<String> fileReviews) {

        String allReviews = String.join("\n\n", fileReviews);

        String summaryPrompt = """
                당신은 숙련된 시니어 백엔드 엔지니어입니다.
                아래는 Pull Request 내 여러 파일에 대한 AI 리뷰 내용입니다.

                이 리뷰들을 기반으로 PR 전체에 대한 '종합 리뷰'를 생성하세요.

                요구사항:
                - 한국어로 작성
                - 기술적 깊이 강조
                - 중요 문제 우선순위 정리
                - 버그 / 성능 / 보안 / 유지보수성 관점에서 평가
                - 다음 개선사항까지 제안
                - markdown 형식 유지

                --------------------
                🔍 파일 리뷰 모음
                --------------------
                %s

                --------------------
                ✨ 출력 형식
                --------------------
                # PR 종합 리뷰

                ## 🔎 주요 변경 요약

                ## 🐞 잠재적 문제 요약

                ## ⚡ 성능 영향 요약

                ## 🔒 보안 검토 요약

                ## 🛠 리팩토링 및 구조 개선 제안

                ## 🧪 추가 테스트 제안

                ## 🎯 종합 평가 및 권장 조치

                절대로 위 템플릿을 벗어나지 마세요.
                """.formatted(allReviews);

        return openAIClient.createChatCompletion(summaryPrompt);
    }

    private String buildPrompt(LLMReviewRequest req) {

        String diffBlock = (req.getDiff() == null || req.getDiff().isEmpty())
                ? "(변경 사항 없음)"
                : String.join("\n", req.getDiff());

        String fullCode = req.getFullCode() == null ? "" : req.getFullCode();

        return """
        당신은 10년 경력의 시니어 백엔드 개발자이며 전문 코드 리뷰어입니다.
        아래 Diff와 전체 코드를 기반으로 실전 프로덕션 환경 수준의 리뷰를 생성하세요.

        ⚠️ 리뷰 지침
        - 반드시 한국어로 작성
        - Diff와 전체 코드를 모두 참고
        - 아래 템플릿 구조를 그대로 유지

        ---------------------
        ## 🔀 DIFF (변경 사항)
        %s

        ## 📄 FULL SOURCE CODE
        %s
        ---------------------

        📌 리뷰 템플릿

        ## 🔍 주요 문제 요약
        - 핵심 문제 요약

        ## 🐞 버그 가능성
        - 오류 가능성이 있는 부분

        ## 🛠 리팩토링 제안
        - 구조 개선, 네이밍 개선, 중복 제거 등

        ## 📚 가독성 향상
        - 복잡한 코드 단순화, 코드 스타일 개선

        ## 🔒 보안 관련 검토
        - 인증/인가, 입력 검증 등

        ## ⚡ 성능 검토
        - 성능 이슈 또는 개선 가능성

        ## 🧪 테스트 누락 분석
        - 필요한 테스트 시나리오

        ## ✨ 종합 평가
        - 전체 평가 및 개선 방향

        위 템플릿을 벗어나지 말고 반드시 한국어로만 답변하세요.
        """.formatted(
                diffBlock,
                fullCode
        );
    }
}