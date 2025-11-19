package com.aicollab.backend.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfo {
    private Long id;
    private String login;
    private String email;
    private String avatarUrl;
}
