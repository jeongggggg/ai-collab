import { Outlet } from "react-router-dom";
import { useAuthStore } from "../store/authStore";
import GlobalLoader from "../components/GlobalLoader";
import "../styles/app-layout.scss";

export default function AppLayout() {
  const { user, logout } = useAuthStore();

  return (
    <div className="app-layout">
      {/* Global Loading Banner */}
      <GlobalLoader />

      {/* Header */}
      <header className="header">
        <h2 className="logo">AI Collab</h2>

        {user && (
          <div className="user-box">
            <img src={user.avatarUrl} alt="avatar" className="avatar" />
            <span className="username">{user.login}</span>
            <button className="logout-btn" onClick={logout}>
              Logout
            </button>
          </div>
        )}
      </header>

      {/* 페이지 콘텐츠 */}
      <main className="content">
        <Outlet />
      </main>
    </div>
  );
}