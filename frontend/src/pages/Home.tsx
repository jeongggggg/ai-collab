import { useEffect, useState } from "react";
import { api } from "../api/client";

export default function Home() {
  const [user, setUser] = useState<any>(null);

  useEffect(() => {
    api.get("/api/auth/me")
      .then(res => setUser(res.data))
      .catch(() => {});
  }, []);

  return (
    <div>
      <h1>Home</h1>
      {user ? (
        <p>Welcome, {user.login}</p>
      ) : (
        <a href="/login">Go to Login</a>
      )}
    </div>
  );
}