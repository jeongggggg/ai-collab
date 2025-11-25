import { useEffect } from "react";
import { api } from "../api/client";

export default function AuthCallback() {
  useEffect(() => {
    const code = new URLSearchParams(window.location.search).get("code");

    if (code) {
      api.post(`/api/auth/exchange?code=${code}`)
        .then(res => {
          const { accessToken } = res.data;
          localStorage.setItem("accessToken", accessToken);
          window.location.href = "/";
        })
        .catch(() => {
          alert("Login failed");
          window.location.href = "/login";
        });
    }
  }, []);

  return <div>Logging in...</div>;
}