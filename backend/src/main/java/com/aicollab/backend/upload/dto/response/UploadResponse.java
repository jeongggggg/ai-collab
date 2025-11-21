package com.aicollab.backend.upload.dto.response;

import com.aicollab.backend.upload.domain.Upload;
import lombok.Getter;

@Getter
public class UploadResponse {

    private final Long id;
    private final String commitSha;
    private final String status;

    public UploadResponse(Upload upload) {
        this.id = upload.getId();
        this.commitSha = upload.getCommitSha();
        this.status = upload.getStatus().name();
    }
}
