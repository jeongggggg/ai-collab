package com.aicollab.backend.project.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectResponse {
    private Long id;
    private String name;
    private String description;

    private OwnerInfo owner;

    @Getter
    @Builder
    public static class OwnerInfo {
        private Long id;
        private String login;
    }
}
