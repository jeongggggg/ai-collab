import { useEffect, useState } from "react";
import { api } from "../../api/client";
import { useParams } from "react-router-dom";
import "../../styles/PrDetail.scss";

interface FileChange {
  filename: string;
  additions: number;
  deletions: number;
  status: string;
}

interface PrReviewFile {
  filename: string;
  review: string;
}

interface PrReviewResult {
  summaryReview: string;
  combinedReviews: string;
  files: PrReviewFile[];
}

export default function PrDetail() {
  const { projectId, prNumber } = useParams();
  const [changedFiles, setChangedFiles] = useState<FileChange[]>([]);
  const [repoOwner, setRepoOwner] = useState("");
  const [repoName, setRepoName] = useState("");

  const [review, setReview] = useState<PrReviewResult | null>(null);
  const [loading, setLoading] = useState(true);

  // 1) 프로젝트 정보 + 파일 리스트 불러오기
  useEffect(() => {
    if (!projectId || !prNumber) return;

    api
      .get(`/api/projects/${projectId}`)
      .then((res) => {
        const p = res.data.data;
        setRepoOwner(p.repoOwner);
        setRepoName(p.repoName);

        return api.get(
          `/api/github/prs/${prNumber}/files?owner=${p.repoOwner}&repo=${p.repoName}`
        );
      })
      .then((res) => {
        setChangedFiles(res.data.data);
      })
      .catch((err) => console.error("❌ Failed to load PR files:", err));
  }, [projectId, prNumber]);

  // 2) PR 분석 자동 실행
  useEffect(() => {
    if (!repoOwner || !repoName) return;

    setLoading(true);

    api
      .post(
        `/api/github/prs/${projectId}/${prNumber}/review?owner=${repoOwner}&repo=${repoName}`
      )
      .then((res) => {
        setReview(res.data.data);
      })
      .catch((err) => console.error("❌ Failed to generate review:", err))
      .finally(() => setLoading(false));
  }, [repoOwner, repoName]);

  return (
    <div className="pr-detail-container">
      <h2>Pull Request #{prNumber}</h2>

      <section>
        <h3>Changed Files</h3>

        {changedFiles.map((file) => (
          <div key={file.filename} className="file-item">
            <div className="file-name">📄 {file.filename}</div>
            <div className="file-meta">
              <span className="add">+{file.additions}</span>
              <span className="del">-{file.deletions}</span>
              <span className="status">{file.status}</span>
            </div>
          </div>
        ))}
      </section>

      <hr />

      {loading && (
        <div className="loading">
          <span>🔍 코드를 분석 중입니다 잠시만 기다려주세요...</span>
        </div>
      )}

      {!loading && review && (
        <>
          <section>
            <h3>PR 전체 Review</h3>
            <pre className="summary-block">{review.summaryReview}</pre>
          </section>

          <section>
            <h3>변경 파일별 Reviews</h3>
            {review.files.map((f) => (
              <div key={f.filename} className="file-review-card">
                <h4>{f.filename}</h4>
                <pre className="review-block">{f.review}</pre>
              </div>
            ))}
          </section>
        </>
      )}
    </div>
  );
}