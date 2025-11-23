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

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.model:gpt-4o-mini}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();

    public OpenAIChatResponse chat(OpenAIChatRequest request) {

        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OpenAIChatRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<OpenAIChatResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                OpenAIChatResponse.class
        );

        return response.getBody();
    }

    public String getDefaultModel() {
        return model;
    }
}
