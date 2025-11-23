package com.aicollab.backend.upload.service;

import com.aicollab.backend.analysis.domain.AnalysisRun;
import com.aicollab.backend.analysis.repository.AnalysisRunRepository;
import com.aicollab.backend.github.service.PrAnalysisService;
import com.aicollab.backend.infrastructure.github.GithubRestClient;
import com.aicollab.backend.project.domain.Project;
import com.aicollab.backend.project.repository.ProjectRepository;
import com.aicollab.backend.upload.domain.Upload;
import com.aicollab.backend.upload.domain.UploadStatus;
import com.aicollab.backend.upload.dto.request.UploadCreateRequest;
import com.aicollab.backend.upload.dto.response.UploadDetailResponse;
import com.aicollab.backend.upload.repository.UploadRepository;
import com.aicollab.backend.user.domain.User;
import com.aicollab.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.aicollab.backend.github.dto.response.PrAnalysisResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UploadService {

    private final ProjectRepository projectRepository;
    private final UploadRepository uploadRepository;
    private final UserRepository userRepository;

    private final GithubRestClient githubRestClient;
    private final PrAnalysisService prAnalysisService;
    private final AnalysisRunRepository analysisRunRepository;

    // 업로드 생성 (owner만 가능)
    public Upload create(Long projectId, Long userId, UploadCreateRequest req) {

        // 프로젝트 확인
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        // 사용자 확인
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 프로젝트 소유자 검증
        if (!project.getOwner().getId().equals(owner.getId())) {
            throw new IllegalArgumentException("User is not owner of this project");
        }

        // commit SHA 유효성 검증
        try {
            githubRestClient.getCommit(project.getOwner().getLogin(), project.getName(), req.getCommitSha());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid commit SHA");
        }

        // 업로드 생성
        Upload upload = Upload.builder()
                .project(project)
                .commitSha(req.getCommitSha())
                .status(UploadStatus.PENDING)
                .build();

        Upload saved = uploadRepository.save(upload);

        // 자동 분석 실행
        runAnalysis(project, saved);

        return saved;
    }

    // 업로드 상세 조회
    public UploadDetailResponse getById(Long projectId, Long uploadId, Long requesterId) {

        Upload upload = uploadRepository.findById(uploadId)
                .orElseThrow(() -> new IllegalArgumentException("Upload not found"));

        if (!upload.getProject().getId().equals(projectId)) {
            throw new IllegalArgumentException("Upload does not belong to project");
        }

        if (!upload.getProject().getOwner().getId().equals(requesterId)) {
            throw new IllegalArgumentException("Not allowed to view this upload");
        }

        List<AnalysisRun> runs = analysisRunRepository.findByUploadId(uploadId);

        return UploadDetailResponse.from(upload, runs);
    }

    // 자동 분석 수행
    private void runAnalysis(Project project, Upload upload) {

        // 1) 분석 실행 로그 생성 (PENDING)
        AnalysisRun run = analysisRunRepository.save(AnalysisRun.createPending(upload));

        // 2) PR 번호 추출
        int prNumber = extractPrNumber(upload.getCommitSha());

        // 3) PR 분석 실행
        var result = prAnalysisService.analyze(
                project.getOwner().getLogin(),
                project.getName(),
                prNumber
        );

        // 4) 분석 완료 처리
        String combinedReviews = result.getFiles().stream()
                .map(PrAnalysisResponse.FileAnalysis::getReview)
                .reduce("", (a, b) -> a + "\n\n" + b);

        run.complete(combinedReviews.trim());

        analysisRunRepository.save(run);

        // 5) 업로드 상태도 완료 처리
        upload.complete();
        uploadRepository.save(upload);
    }

    // commit SHA -> PR 번호 추출 (임시)
    private int extractPrNumber(String sha) {
        try {
            return Integer.parseInt(sha.trim());
        } catch (Exception e) {
            return 1;
        }
    }
}