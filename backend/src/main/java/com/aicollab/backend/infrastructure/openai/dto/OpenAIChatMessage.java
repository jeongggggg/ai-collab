package com.aicollab.backend.infrastructure.openai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OpenAIChatMessage {
    private String role;
    private String content; // 프롬프트 내용
}
