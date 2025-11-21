package com.aicollab.backend.upload.controller;

import com.aicollab.backend.auth.security.UserPrincipal;
import com.aicollab.backend.global.response.ApiResponse;
import com.aicollab.backend.upload.domain.Upload;
import com.aicollab.backend.upload.dto.request.UploadCreateRequest;
import com.aicollab.backend.upload.dto.response.UploadResponse;
import com.aicollab.backend.upload.repository.UploadRepository;
import com.aicollab.backend.upload.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/uploads")
public class UploadController {

    private final UploadService uploadService;

    @PostMapping
    public ApiResponse<UploadResponse> createUpload(
            @PathVariable Long projectId,
            @RequestBody UploadCreateRequest req,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        Upload upload = uploadService.create(projectId, principal.getId(), req);
        return ApiResponse.success(new UploadResponse(upload));
    }
}
