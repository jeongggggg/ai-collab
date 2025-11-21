package com.aicollab.backend.upload.controller;

import com.aicollab.backend.auth.security.UserPrincipal;
import com.aicollab.backend.global.response.ApiResponse;
import com.aicollab.backend.upload.dto.request.UploadCreateRequest;
import com.aicollab.backend.upload.dto.response.UploadDetailResponse;
import com.aicollab.backend.upload.dto.response.UploadResponse;
import com.aicollab.backend.upload.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/uploads")
public class UploadController {

    private final UploadService uploadService;

    // 업로드 생성
    @PostMapping
    public ApiResponse<UploadResponse> createUpload(
            @PathVariable Long projectId,
            @RequestBody UploadCreateRequest req,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        var upload = uploadService.create(projectId, principal.getId(), req);
        return ApiResponse.success(new UploadResponse(upload));
    }

    // 업로드 상세 조회
    @GetMapping("/{uploadId}")
    public ApiResponse<UploadDetailResponse> getUpload(
            @PathVariable Long projectId,
            @PathVariable Long uploadId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        UploadDetailResponse response =
                uploadService.getById(projectId, uploadId, principal.getId());

        return ApiResponse.success(response);
    }
}