import { useEffect, useState } from "react";
import { api } from "../api/client";
import { useNavigate } from "react-router-dom";
import "../styles/Home.scss";
import "../styles/common-loading.scss";


export default function Home() {
  const navigate = useNavigate();

  const [projects, setProjects] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api
      .get("/api/projects")
      .then((res) => setProjects(res.data.data))
      .catch((err) => console.error("❌ Failed to load projects:", err))
      .finally(() => setLoading(false));
  }, []);

 if (loading)
  return (
    <div className="fullscreen-loading">
      <div className="spinner"></div>
      <div className="text">Loading...</div>
    </div>
  );

  return (
    <div className="home-container">
      <div className="home-header">
        <h2>내 프로젝트 목록</h2>
        <button className="new-btn" onClick={() => navigate("/projects/new")}>
          + New Project
        </button>
      </div>

      <div className="project-grid">
        {projects.map((p: any) => (
          <div
            key={p.id}
            className="project-card"
            onClick={() => navigate(`/projects/${p.id}`)}
          >
            <div className="project-title">{p.name}</div>

            <div className="project-description">
              {p.description || "No description"}
            </div>

            <div className="project-repo">
              {p.repoOwner}/{p.repoName}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}