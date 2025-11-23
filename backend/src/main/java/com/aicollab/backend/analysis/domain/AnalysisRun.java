package com.aicollab.backend.analysis.domain;

import com.aicollab.backend.upload.domain.Upload;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AnalysisRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Upload upload;

    @Enumerated(EnumType.STRING)
    private AnalysisStatus status;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String review;   // 파일별 상세 리뷰 전체

    @Lob
    @Column(columnDefinition = "TEXT")
    private String summaryReview;  // PR 전체 요약 리뷰

    private LocalDateTime createdAt;

    public static AnalysisRun createPending(Upload upload) {
        return AnalysisRun.builder()
                .upload(upload)
                .status(AnalysisStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void complete(String review, String summary) {
        this.status = AnalysisStatus.COMPLETED;
        this.review = review;
        this.summaryReview = summary;
    }
}