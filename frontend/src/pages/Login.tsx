import { Navigate } from "react-router-dom";
import { useAuthStore } from "../store/authStore";

export default function Login() {
  const user = useAuthStore((state) => state.user);

  if (user) return <Navigate to="/" replace />;

  const CLIENT_ID = import.meta.env.VITE_GITHUB_CLIENT_ID;
  const REDIRECT_URI = "http://localhost:5173/auth/callback";

  const loginWithGithub = () => {
    window.location.href =
      `https://github.com/login/oauth/authorize?client_id=${CLIENT_ID}` +
      `&redirect_uri=${encodeURIComponent(REDIRECT_URI)}` +
      `&scope=read:user user:email`;
  };

  return (
    <div style={{ textAlign: "center", marginTop: "100px" }}>
      <h1>AI Collab 로그인</h1>
      <p>GitHub 계정으로 로그인하세요.</p>

      <button
        onClick={loginWithGithub}
        style={{
          marginTop: "20px",
          padding: "10px 20px",
          fontSize: "16px",
          cursor: "pointer",
        }}
      >
        GitHub Login
      </button>
    </div>
  );
}