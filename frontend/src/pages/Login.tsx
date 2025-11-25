import { useAuthStore } from "../store/authStore";
import { Navigate } from "react-router-dom";

export default function Login() {
  const user = useAuthStore((state) => state.user);
  const CLIENT_ID = "Ov23liXibuU5rGDfRWZg";
  const REDIRECT_URI = "http://localhost:5173/auth/callback";

  if (user) return <Navigate to="/" replace />;

  const loginWithGithub = () => {
    window.location.href =
      `https://github.com/login/oauth/authorize?client_id=${CLIENT_ID}&redirect_uri=${REDIRECT_URI}&scope=read:user user:email`;
  };

  return (
    <div>
      <h1>Login</h1>
      <button onClick={loginWithGithub}>Login with GitHub</button>
    </div>
  );
}