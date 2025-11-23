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

        return """
                당신은 10년 경력의 시니어 백엔드 코드 리뷰어입니다.
                아래 Diff 코드를 기반으로 명확하고 실용적인 코드 리뷰를 작성하세요.

                반드시 포함할 항목:
                - 잠재적 버그
                - 리팩토링 필요성
                - 성능 영향
                - 보안 관련 문제
                - 더 나은 구현 아이디어 제안

                파일명: %s

                --- DIFF START ---
                %s
                --- DIFF END ---

                리뷰는 한국어로 작성하세요.
                """.formatted(req.getFilename(), diffBlock);
    }
}
