package com.aicollab.backend.project.dto.request;

import lombok.Getter;

@Getter
public class ProjectCreateRequest {
    private String name;
    private String description;
    private String repoOwner;
    private String repoName;
}
