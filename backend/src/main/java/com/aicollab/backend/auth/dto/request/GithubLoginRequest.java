package com.aicollab.backend.auth.dto.request;

public class GithubLoginRequest {

    private String code;
    private String redirectUrl;

    protected GithubLoginRequest() {

    }

    public GithubLoginRequest(String code, String redirectUrl) {
        this.code = code;
        this.redirectUrl = redirectUrl;
    }

    public String getCode() {
        return code;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

}
