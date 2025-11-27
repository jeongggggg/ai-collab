import { Navigate } from "react-router-dom";
import { useAuthStore } from "../store/authStore";
import "../styles/login.scss";

export default function Login() {
  const user = useAuthStore((state) => state.user);

  if (user) return <Navigate to="/" replace />;

  const CLIENT_ID = import.meta.env.VITE_GITHUB_CLIENT_ID;
  const REDIRECT_URI = "http://localhost:5173/auth/callback";

  const loginWithGithub = () => {
    const url =
      `https://github.com/login/oauth/authorize?client_id=${CLIENT_ID}` +
      `&redirect_uri=${encodeURIComponent(REDIRECT_URI)}` +
      `&scope=read:user user:email`;
    window.location.href = url;
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <h1 className="title">AI Collab</h1>
        <p className="subtitle">GitHub 계정으로 로그인하세요</p>

        <button className="github-btn" onClick={loginWithGithub}>
          GitHub로 로그인
        </button>
      </div>
    </div>
  );
}