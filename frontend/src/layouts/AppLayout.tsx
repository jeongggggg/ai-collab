import { Outlet } from "react-router-dom";
import { useAuthStore } from "../store/authStore";
import GlobalLoader from "../components/GlobalLoader";

export default function AppLayout() {
  const { user, logout } = useAuthStore();

  return (
    <div style={{ padding: "20px" }}>
      {/* Global Loading Banner */}
      <GlobalLoader />

      {/* Header */}
      <header
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          marginBottom: "20px",
          borderBottom: "1px solid #ddd",
          paddingBottom: "10px",
        }}
      >
        <h2 style={{ margin: 0 }}>AI Collab</h2>

        {user && (
          <div style={{ display: "flex", alignItems: "center", gap: "10px" }}>
            <img
              src={user.avatarUrl}
              alt="avatar"
              style={{
                width: "32px",
                height: "32px",
                borderRadius: "50%",
              }}
            />
            <span>{user.login}</span>
            <button
              onClick={logout}
              style={{
                padding: "6px 12px",
                cursor: "pointer",
              }}
            >
              Logout
            </button>
          </div>
        )}
      </header>

      {/* 페이지 콘텐츠 */}
      <Outlet />
    </div>
  );
}