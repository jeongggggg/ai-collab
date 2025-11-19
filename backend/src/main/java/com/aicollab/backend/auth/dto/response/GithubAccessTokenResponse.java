package com.aicollab.backend.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GithubAccessTokenResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    private String scope;

    protected GithubAccessTokenResponse() {}

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getScope() {
        return scope;
    }
}
