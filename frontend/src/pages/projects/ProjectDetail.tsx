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

  if (loading) return <div>Loading...</div>;
  if (!project) return <div>Project Not Found</div>;

  return (
    <div className="project-detail-container">
      <h2>{project.name}</h2>
      <p className="description">{project.description}</p>

      <h3>Pull Requests</h3>

      {prs.length === 0 && <div>No Pull Requests found.</div>}

      <ul className="pr-list">
        {prs.map((pr) => (
          <li
            key={pr.number}
            className="pr-item"
            onClick={() => navigate(`/projects/${id}/prs/${pr.number}`)}
          >
            <div className="pr-title">
              #{pr.number} - {pr.title}
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