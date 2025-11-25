import { useEffect, useState } from "react";
import { api } from "../../api/client";
import { useParams, useNavigate } from "react-router-dom";
import "../../styles/ProjectDetail.scss";

// 프로젝트 정보 타입
interface Project {
  id: number;
  name: string;
  description: string;
  repoOwner: string;
  repoName: string;
}

// PR 타입
interface PullRequest {
  number: number;
  title: string;
  state: string;
  user?: {
    login: string;
  };
}

export default function ProjectDetail() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [project, setProject] = useState<Project | null>(null);
  const [prs, setPrs] = useState<PullRequest[]>([]);
  const [loading, setLoading] = useState(true);

  // 프로젝트 정보 + PR 목록 조회
  useEffect(() => {
    if (!id) return;

    api
      .get(`/api/projects/${id}`)
      .then((res) => {
        const data = res.data.data;
        console.log("📌 Project Detail:", data);

        setProject(data);

        // repoOwner / repoName 존재하는지 확인
        if (!data.repoOwner || !data.repoName) {
          console.error("❌ repoOwner 또는 repoName 없음:", data);
          return null;
        }

        // PR 목록 조회
        return api.get(
          `/api/github/prs?owner=${data.repoOwner}&repo=${data.repoName}`
        );
      })
      .then((res) => {
        if (!res) return; // 위에서 null일 때 방지

        console.log("📌 PR LIST:", res.data.data);
        setPrs(res.data.data || []);
      })
      .catch((err) => {
        console.error("❌ 프로젝트 or PR 조회 실패:", err);
      })
      .finally(() => setLoading(false));
  }, [id]);

  if (loading) return <div className="loading">Loading...</div>;
  if (!project) return <div className="loading">Project not found.</div>;

  return (
    <div className="project-detail-container">
      <h2>{project.name}</h2>
      <p className="description">{project.description}</p>

      <h3>Pull Requests</h3>

      {prs.length === 0 && (
        <div className="no-pr">No pull requests found for this repository.</div>
      )}

      <ul className="pr-list">
        {prs.map((pr) => (
          <li
            key={pr.number}
            className="pr-item"
            onClick={() => navigate(`/projects/${id}/prs/${pr.number}`)}
          >
            <div className="pr-title">
              #{pr.number} — {pr.title}
            </div>
            <div className="pr-meta">
              <span>Author: {pr.user?.login}</span>
              <span>Status: {pr.state}</span>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}