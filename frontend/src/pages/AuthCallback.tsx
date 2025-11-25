import { useEffect } from "react";
import { api } from "../api/client";
import { useAuthStore } from "../store/authStore";

export default function AuthCallback() {
  const fetchMe = useAuthStore((state) => state.fetchMe);

  useEffect(() => {
    const code = new URLSearchParams(window.location.search).get("code");
    if (!code) return;

    // 중복 요청 방지
    if (sessionStorage.getItem("oauth_code") === code) {
      return;
    }
    sessionStorage.setItem("oauth_code", code);

    api.post("/api/auth/exchange", null, { params: { code } })
      .then(async (res) => {
        const { accessToken } = res.data;
        localStorage.setItem("accessToken", accessToken);

        await fetchMe();
        window.location.href = "/";
      })
      .catch((err) => {
        console.error("exchange error:", err);
        alert("Login failed");
        window.location.href = "/login";
      });
  }, []);

  return <div>Logging in...</div>;
}