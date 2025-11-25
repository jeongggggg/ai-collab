package com.aicollab.backend.github.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubRepoResponse {
    private Long id;
    private String name;
    private boolean isPrivate;
    private String htmlUrl;
}