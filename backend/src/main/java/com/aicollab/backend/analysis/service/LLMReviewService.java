package com.aicollab.backend.analysis.service;

import com.aicollab.backend.analysis.dto.request.LLMReviewRequest;
import com.aicollab.backend.analysis.dto.response.LLMReviewResponse;
import org.springframework.stereotype.Service;

@Service
public class LLMReviewService {

    public LLMReviewResponse generateReview(LLMReviewRequest req) {

        // TODO: 실제 GPT API 연동은 이후 단계에서 적용
        String fakeReview = """
                # 자동 생성된 코드 리뷰 (샘플)
                
                - 파일명: %s
                - 변경된 라인 수: %d
                
                개선할 수 있는 사항이 더 있을 수 있습니다.
                """.formatted(req.getFilename(), req.getDiff().size());

        return new LLMReviewResponse(fakeReview);
    }
}
