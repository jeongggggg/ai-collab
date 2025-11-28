package com.aicollab.backend.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "github")
@Getter
@Setter
public class GithubOAuthProperties {

    private String clientId;
    private String clientSecret;
    private String redirectUrl;
}