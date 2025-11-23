package com.aicollab.backend.analysis.service;

import com.aicollab.backend.analysis.dto.request.LLMReviewRequest;
import com.aicollab.backend.analysis.dto.response.LLMReviewResponse;
import com.aicollab.backend.infrastructure.openai.OpenAIClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LLMReviewService {

    private final OpenAIClient openAIClient;

    public LLMReviewResponse generateReview(LLMReviewRequest req) {

        String prompt = buildPrompt(req);
        String reviewText = openAIClient.createChatCompletion(prompt);

        return new LLMReviewResponse(reviewText);
    }

    private String buildPrompt(LLMReviewRequest req) {

        String diffBlock = String.join("\n", req.getDiff());
        String fullCode = req.getFullCode() == null ? "" : req.getFullCode();

        return """
                You are a senior backend engineer and professional code reviewer.
                Provide accurate and practical feedback based on BOTH the given diff and the full source code.

                ⚠️ Important Rules:
                1. Always review based on BOTH diff changes and full code context.
                2. Respond in Korean.
                3. Output MUST follow the structured markdown template.
                4. Cite exact line numbers when referring to code.
                5. If a commit message is included in the diff, evaluate whether the commit type follows the rules.

                📄 Commit Type Convention (검증 기준)
                ✨ Feat — 새로운 기능 추가
                🐛 Fix — 버그 수정
                🧹 Refactor — 리팩터링 (기능 변화 없음)
                🎨 Style — 코드 스타일/포맷 수정
                🧪 Test — 테스트 코드 추가/수정
                📄 Docs — 문서 수정
                🔧 Chore — 설정/빌드/패키지 변경
                🚀 Deploy — 배포 관련 변경
                🧩 Config — 환경 설정 변경
                🗑️ Remove — 삭제 작업

                📑 커밋 메시지 분석 시 체크 사항:
                - 커밋 타입이 작업 내용과 의미적으로 일치하는가?
                - 여러 타입의 작업인데 단일 타입만 사용된 것은 아닌가?
                - 이모지 및 타입 네이밍 규칙을 따르는가?
                - 지나치게 모호하거나 광범위한 커밋 메시지는 아닌가?

                ---------------------
                ✨ 파일명: %s
                ---------------------

                ## 🔀 DIFF (변경 사항)
                %s

                ## 📄 FULL SOURCE CODE
                %s

                ---------------------
                📌 아래 템플릿을 STRICT하게 따르세요:

                # 파일명: %s

                ## 🔍 주요 문제 요약
                - 핵심 문제 요약

                ## 🐞 버그 가능성
                - 실행 중 오류 가능성이 있는 부분

                ## 🛠 리팩토링 제안
                - 구조 개선, 네이밍 개선, 중복 제거 등

                ## 📚 가독성 향상
                - 복잡한 코드 단순화, 코드 스타일 개선

                ## 🔒 보안 관련 검토
                - 인증/인가, 입력 검증, 민감정보 검토

                ## ⚡ 성능 검토
                - 성능 이슈 또는 개선 가능성

                ## 🧪 테스트 누락 분석
                - 추가 필요 테스트 시나리오 제안

                ## 🗂 커밋 타입 적합성 검토
                - 적용된 커밋 타입이 규칙에 맞는지 분석
                - 부적절한 경우 올바른 타입 제안

                ## ✨ 종합 평가
                - 전체 리뷰 요약 및 개선 방향
                ---------------------

                반드시 위 템플릿을 벗어나지 말고 한국어로만 답변하세요.
                """.formatted(
                req.getFilename(),
                diffBlock,
                fullCode,
                req.getFilename()
        );
    }
}