import { useEffect, useState } from "react";
import { api } from "../api/client";
import { useNavigate } from "react-router-dom";
import "../styles/Home.scss";

export default function Home() {
  const navigate = useNavigate();

  const [projects, setProjects] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api
      .get("/api/projects")
      .then((res) => {
        setProjects(res.data.data);
      })
      .catch((err) => {
        console.error("❌ Failed to load projects:", err);
      })
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div>Loading projects...</div>;

  return (
    <div className="home-container">
      <div className="home-header">
        <h2>Your Projects</h2>
        <button className="new-btn" onClick={() => navigate("/projects/new")}>
          + New Project
        </button>
      </div>

      <div className="project-list">
        {projects.map((p: any) => (
          <div
            key={p.id}
            className="project-item"
            onClick={() => navigate(`/projects/${p.id}`)}
          >
            <div className="project-name">{p.name}</div>
            <div className="project-desc">{p.description}</div>
            <div className="project-repo">
              {p.repoOwner}/{p.repoName}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}