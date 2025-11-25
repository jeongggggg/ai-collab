import { Outlet } from "react-router-dom";
import { useAuthStore } from "../store/authStore";

export default function AppLayout() {
  const { user, logout } = useAuthStore();

  return (
    <div style={{ padding: "20px" }}>
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
        <h2>AI Collab</h2>

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
            <button onClick={logout}>Logout</button>
          </div>
        )}
      </header>

      {/* Content */}
      <Outlet />
    </div>
  );
}