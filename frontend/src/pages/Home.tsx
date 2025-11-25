import { useAuthStore } from "../store/authStore";

export default function Home() {
  const { user, logout, isLoading } = useAuthStore();

  if (isLoading) return <div>Loading...</div>;

  if (!user)
    return (
      <div>
        <a href="/login">Login</a>
      </div>
    );

  return (
    <div>
      <p>Welcome, {user.login}</p>
      <button onClick={logout}>Logout</button>
    </div>
  );
}