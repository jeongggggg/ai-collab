package com.aicollab.backend.infrastructure.openai.dto.request;

import com.aicollab.backend.infrastructure.openai.dto.OpenAIMessage;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OpenAIChatRequest {
    private String model;
    private List<OpenAIMessage> messages;
    private double temperature;

    public static OpenAIChatRequest of(String prompt) {
        return OpenAIChatRequest.builder()
                .model("gpt-4o-mini")
                .temperature(0.2)
                .messages(List.of(
                        new OpenAIMessage("user", prompt)
                ))
                .build();
    }
}
