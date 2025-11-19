package com.aicollab.backend.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "github")
public class GithubOAuthProperties {

    private String clientId;
    private String clientSecret;
    private String redirectIrl;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRedirectIrl() {
        return redirectIrl;
    }

    public void setRedirectIrl(String redirectIrl) {
        this.redirectIrl = redirectIrl;
    }
}
