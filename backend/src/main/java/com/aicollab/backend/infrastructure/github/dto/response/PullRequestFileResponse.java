package com.aicollab.backend.infrastructure.github.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class PullRequestFileResponse {

    private String filename;

    @JsonProperty("status")
    private String status;

    @JsonProperty("additions")
    private int additions;

    @JsonProperty("deletions")
    private int deletions;

    @JsonProperty("changes")
    private int changes;

    @JsonProperty("blob_url")
    private String blobUrl;

    @JsonProperty("raw_url")
    private String rawUrl;

    @JsonProperty("sha")
    private String sha;

    @JsonProperty("patch")
    private String patch;

}
