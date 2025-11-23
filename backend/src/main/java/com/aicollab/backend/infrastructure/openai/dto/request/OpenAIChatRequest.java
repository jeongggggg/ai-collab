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
}
