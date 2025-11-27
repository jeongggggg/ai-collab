import { useEffect, useState } from "react";
import { api } from "../../api/client";
import { useParams } from "react-router-dom";
import "../../styles/PrDetail.scss";
import "../../styles/common-loading.scss";
import ReactMarkdown from "react-markdown";

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

  // 1) 프로젝트 기반 파일 리스트 로딩
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

  if (loading)
    return (
      <div className="fullscreen-loading">
        <div className="spinner"></div>
        <div className="text">
          Pull Request 분석 중...
        </div>
      </div>
    );

  return (
    <div className="pr-detail-container">
      <h2>Pull Request #{prNumber}</h2>

      {/* Changed File List */}
      <section className="file-section">
        <h3>Changed Files</h3>

        {changedFiles.length === 0 && (
          <div className="empty-text">No changed files.</div>
        )}

        <div className="file-list">
          {changedFiles.map((file) => (
            <div key={file.filename} className="file-card">
              <div className="file-left">
                <div className="file-name">📄 {file.filename}</div>
              </div>

              <div className="file-right">
                <span className="add">+{file.additions}</span>
                <span className="del">-{file.deletions}</span>
                <span className={`status ${file.status}`}>{file.status}</span>
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* Review Sections */}
      {review && (
          <>
            <section className="review-section">
              <h3>PR Summary Review</h3>

              <div className="markdown-body">
                <ReactMarkdown>{review.summaryReview}</ReactMarkdown>
              </div>
            </section>

            <section className="review-section">
              <h3>File-by-file Reviews</h3>

              {review.files.map((f) => (
                <div key={f.filename} className="file-review-card">
                  <h4>{f.filename}</h4>
                  <div className="markdown-body">
                    <ReactMarkdown>{f.review}</ReactMarkdown>
                  </div>
                </div>
              ))}
            </section>
          </>
        )}
    </div>
  );
}