import { useEffect, useState } from "react";
import { api } from "../../api/client";
import { useNavigate } from "react-router-dom";
import "../../styles/ProjectCreate.scss";
import "../../styles/common-loading.scss"

interface Repo {
  id: number;
  name: string;
  full_name: string;
  owner: {
    login: string;
  };
}

export default function ProjectCreate() {
  const navigate = useNavigate();

  const [repos, setRepos] = useState<Repo[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedRepo, setSelectedRepo] = useState("");

  const [name, setName] = useState("");
  const [description, setDescription] = useState("");

  useEffect(() => {
    api.get("/api/github/repos")
      .then((res) => {

        console.log("API RAW RESPONSE: ", res.data);  
        console.log("repos array: ", res.data.data);  
        console.log("repos[0]: ", res.data.data?.[0]);

        setRepos(res.data.data); // ApiResponse.data

      })
      .catch((err) => {
        console.error("Failed to load repos:", err);
        alert("GitHub 저장소를 불러오는데 실패했습니다.");
      })
      .finally(() => setLoading(false));
  }, []);

  useEffect(() => {
    console.log("RENDER repos:", repos);
  }, [repos]);

  const submit = async () => {
    if (!selectedRepo) {
      alert("레포를 선택해주세요.");
      return;
    }

    const repo = repos.find((r) => r.full_name === selectedRepo);
    console.log("🟦 Selected Repo Object:", repo);

    if (!repo) return;

    const payload = {
      name,
      description,
      repoOwner: repo.owner.login,
      repoName: repo.name,
    };

    console.log("🟩 Final Payload:", payload);

    try {
      await api.post("/api/projects", payload);
      alert("프로젝트가 생성되었습니다!");
      navigate("/");
    } catch (err) {
      console.error("Project create failed:", err);
      alert("프로젝트 생성 실패");
    }
  };

  if (loading)
    return (
      <div className="fullscreen-loading">
        <div className="spinner"></div>
        <div className="text">Loading...</div>
      </div>
    );

  return (
    <div className="project-create-page">
      <div className="project-create-container">
        <h2>Create a New Project</h2>

        <div className="form-wrapper">
          <label>Project Name</label>
          <input
            className="input"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />

          <label>Description</label>
          <textarea
            className="textarea"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          />

          <label>GitHub Repository</label>
          <select
            className="select"
            value={selectedRepo}
            onChange={(e) => {
              console.log("Selected value:", e.target.value);
              setSelectedRepo(e.target.value);
            }}
          >
            <option value="">Choose repository</option>

            {repos.map((repo) => {
              console.log("Rendering option:", repo);
              return (
                <option key={repo.id} value={repo.full_name}>
                  {repo.full_name}
                </option>
              );
            })}
          </select>

          <button className="submit-btn" onClick={submit}>
            Create Project
          </button>
        </div>
      </div>  
    </div>
  );
}