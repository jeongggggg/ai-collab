import { useEffect, useState } from "react";
import { api } from "../../api/client";
import { useParams, useNavigate } from "react-router-dom";
import "../../styles/ProjectDetail.scss";

interface Project {
  id: number;
  name: string;
  description: string;
  repoOwner: string;
  repoName: string;
}

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

  useEffect(() => {
    if (!id) return;

    api
      .get(`/api/projects/${id}`)
      .then((res) => {
        const data = res.data.data;
        setProject(data);

        if (!data.repoOwner || !data.repoName) return null;

        return api.get(
          `/api/github/prs?owner=${data.repoOwner}&repo=${data.repoName}`
        );
      })
      .then((res) => {
        if (res) setPrs(res.data.data || []);
      })
      .catch((err) => console.error(err))
      .finally(() => setLoading(false));
  }, [id]);

    const deleteProject = async () => {
    const ok = window.confirm("정말 삭제하시겠습니까?");
    if (!ok) return;

    try {
      await api.delete(`/api/projects/${id}`);
      alert("프로젝트가 삭제되었습니다.");
      navigate("/");
    } catch (err: any) {
        console.error(err);

        const message =
          err.response?.data?.message ||
          "삭제에 실패했습니다.";

        alert(message);
      }
  };

  if (loading)
    return (
      <div className="fullscreen-loading">
        <div className="spinner" />
        <div className="text">Loading project...</div>
      </div>
    );

  if (!project) return <div className="not-found">Project Not Found</div>;

  return (
    <div className="project-detail-container">
      <div className="project-header">
        <div className="project-info">
          <h2>{project.name}</h2>
          <p className="description">{project.description}</p>
        </div>

        <a
          className="repo-link"
          href={`https://github.com/${project.repoOwner}/${project.repoName}`}
          target="_blank"
          rel="noopener noreferrer"
        >
          🔗 View on GitHub
        </a>
      </div>

      <h3 className="section-title">Pull Requests</h3>

      {prs.length === 0 && (
        <div className="empty-text">No Pull Requests found.</div>
      )}

      <ul className="pr-list">
        {prs.map((pr) => (
          <li
            key={pr.number}
            className="pr-item"
            onClick={() => navigate(`/projects/${id}/prs/${pr.number}`)}
          >
            <div className="pr-title">
              <span className="pr-number">#{pr.number}</span>
              <span>{pr.title}</span>
            </div>

            <div className="pr-meta">
              <span>Author: {pr.user?.login}</span>
              <span className={`pr-state ${pr.state}`}>{pr.state}</span>
            </div>
          </li>
        ))}
      </ul>
      <button className="delete-btn" onClick={deleteProject}>Delete</button>
    </div>
  );
}