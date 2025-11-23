package com.aicollab.backend.infrastructure.openai;

import com.aicollab.backend.infrastructure.openai.dto.request.OpenAIChatRequest;
import com.aicollab.backend.infrastructure.openai.dto.response.OpenAIChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class OpenAIClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${openai.api-key}")
    private String apiKey;

    public String createChatCompletion(String prompt) {

        OpenAIChatRequest body = OpenAIChatRequest.of(prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<OpenAIChatRequest> entity = new HttpEntity<>(body, headers);

        ResponseEntity<OpenAIChatResponse> response =
                restTemplate.postForEntity(
                        "https://api.openai.com/v1/chat/completions",
                        entity,
                        OpenAIChatResponse.class
                );

        return response.getBody()
                .getChoices()
                .get(0)
                .getMessage()
                .getContent();
    }
}
