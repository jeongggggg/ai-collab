package com.aicollab.backend.auth.dto.response;

public class AuthResponse {

    private String accessToken; // JWT
    private final UserInfo user;

    private AuthResponse(String accessToken, UserInfo user) {
        this.accessToken = accessToken;
        this.user = user;
    }

    public static AuthResponse of(String accessToken, Long id, String login, String email, String avatarUrl) {
        return new AuthResponse(
                accessToken,
                new UserInfo(id, login, email, avatarUrl)
        );
    }
}
