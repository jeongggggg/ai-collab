import { useEffect, useState } from "react";
import { api } from "../../api/client";
import { useParams, useNavigate } from "react-router-dom";
import "../../styles/prdetail.scss";

interface PrInfo {
  number: number;
  title: string;
  state: string;
}

interface PrFile {
  filename: string;
  status: string;
  additions: number;
  deletions: number;
  changes: number;
}

export default function PrDetail() {
  const { projectId, prNumber } = useParams();
  const navigate = useNavigate();

  const [project, setProject] = useState<any>(null);
  const [pr, setPr] = useState<PrInfo | null>(null);
  const [files, setFiles] = useState<PrFile[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!projectId || !prNumber) return;

    // 1) 프로젝트 상세 조회 → repoOwner, repoName 가져오기
    api
      .get(`/api/projects/${projectId}`)
      .then((res) => {
        const p = res.data.data;
        setProject(p);

        // 2) PR 정보 + 파일 목록 조회
        return api.get(
          `/api/github/prs/${prNumber}/files?owner=${p.repoOwner}&repo=${p.repoName}`
        );
      })
      .then((res) => {
        setFiles(res.data.data);
      })
      .catch((err) => {
        console.error("❌ Failed to load PR files:", err);
      })
      .finally(() => setLoading(false));
  }, [projectId, prNumber]);

  if (loading) return <div className="loading">Loading...</div>;

  return (
    <div className="pr-detail-container">
      <h2>Pull Request #{prNumber}</h2>

      <h3>Files Changed</h3>

      {files.length === 0 && (
        <div className="no-files">No files found in this PR.</div>
      )}

      <ul className="file-list">
        {files.map((file) => (
          <li
            key={file.filename}
            className="file-item"
            onClick={() =>
              navigate(`/projects/${projectId}/prs/${prNumber}/files/${encodeURIComponent(file.filename)}`)
            }
          >
            <div className="file-name">{file.filename}</div>
            <div className="file-meta">
              <span>{file.status}</span>
              <span>+{file.additions}</span>
              <span>-{file.deletions}</span>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}