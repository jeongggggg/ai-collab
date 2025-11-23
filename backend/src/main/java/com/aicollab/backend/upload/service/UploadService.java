package com.aicollab.backend.upload.service;

import com.aicollab.backend.analysis.domain.AnalysisRun;
import com.aicollab.backend.analysis.repository.AnalysisRunRepository;
import com.aicollab.backend.github.service.GithubService;
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
    private final GithubService githubService;

    private final PrAnalysisService prAnalysisService;
    private final AnalysisRunRepository analysisRunRepository;

    // 업로드 생성
    public Upload create(Long projectId, Long userId, UploadCreateRequest req) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!project.getOwner().getId().equals(owner.getId())) {
            throw new IllegalArgumentException("User is not owner of this project");
        }

        // commit SHA 검증
        try {
            githubRestClient.getCommit(owner.getLogin(), project.getName(), req.getCommitSha());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid commit SHA");
        }

        // PR 번호 자동 조회
        Integer prNumber = githubService.getPrNumberByCommit(
                owner.getLogin(),
                project.getName(),
                req.getCommitSha()
        );

        if (prNumber == null) {
            throw new IllegalArgumentException("No Pull Request found for given commit");
        }

        // 업로드 생성
        Upload upload = Upload.builder()
                .project(project)
                .commitSha(req.getCommitSha())
                .status(UploadStatus.PENDING)
                .build();

        Upload saved = uploadRepository.save(upload);

        // 자동 분석
        runAnalysis(project, saved, prNumber);

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

    // 자동 분석 실행
    private void runAnalysis(Project project, Upload upload, int prNumber) {

        // 분석 실행 로그 생성
        AnalysisRun run = analysisRunRepository.save(AnalysisRun.createPending(upload));

        // PR 분석 수행
        PrAnalysisResponse result = prAnalysisService.analyze(
                project.getOwner().getLogin(),
                project.getName(),
                prNumber
        );

        // 리뷰 텍스트 합치기
        String combinedReviews = result.getFiles().stream()
                .map(PrAnalysisResponse.FileAnalysis::getReview)
                .reduce("", (a, b) -> a + "\n\n" + b);

        run.complete(combinedReviews.trim());
        analysisRunRepository.save(run);

        upload.complete();
        uploadRepository.save(upload);
    }
}