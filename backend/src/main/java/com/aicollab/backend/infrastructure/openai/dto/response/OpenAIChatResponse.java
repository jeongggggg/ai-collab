package com.aicollab.backend.infrastructure.openai.dto.response;

import lombok.Getter;
import org.aspectj.bridge.Message;

import java.awt.*;
import java.util.List;

@Getter
public class OpenAIChatResponse {

    private List<Choice> choices;

    @Getter
    public static class Choice {
        private Message message;
    }

    @Getter
    public static class Message {
        private String role;
        private String content;
    }
}
