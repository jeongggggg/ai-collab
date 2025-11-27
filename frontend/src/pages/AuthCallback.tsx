import { useEffect } from "react";
import { api } from "../api/client";
import { useAuthStore } from "../store/authStore";
import "../styles/common-loading.scss";

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

    api
      .post("/api/auth/exchange", null, { params: { code } })
      .then(async (res) => {
        const { accessToken } = res.data;
        localStorage.setItem("accessToken", accessToken);

        await fetchMe();
        window.location.href = "/";
      })
      .catch(() => {
        alert("Login failed");
        window.location.href = "/login";
      });
  }, []);

  return (
    <div className="fullscreen-loading">
      <div className="spinner"></div>
      <div className="text">GitHub 로그인 처리 중...</div>
    </div>
  );
}