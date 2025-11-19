package com.aicollab.backend.auth.dto.response;

public class AuthResponse {

    private String accessToken; // JWT
    private Long userId;
    private String login;
    private String email;
    private String avatarUrl;

    protected AuthResponse(){}

    public AuthResponse(String accessToken, Long userId, String login, String email, String avatarUrl) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.login = login;
        this.email = email;
        this.avatarUrl = avatarUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Long getUserId() {
        return userId;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
