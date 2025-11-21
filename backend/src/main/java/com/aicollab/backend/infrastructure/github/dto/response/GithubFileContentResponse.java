package com.aicollab.backend.infrastructure.github.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class GithubFileContentResponse {

    private String name;
    private String path;
    private String sha;

    @JsonProperty("content")
    private String encodedContent; // base64 encoded

    @JsonProperty("encoding")
    private String encoding;
}
