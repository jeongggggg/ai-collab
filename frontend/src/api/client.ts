import axios from "axios";
import { useUIStore } from "../store/uiStore";

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  withCredentials: true,
});

// 요청 전: 로딩 시작
api.interceptors.request.use((config) => {
  useUIStore.getState().setGlobalLoading(true);

  const token = localStorage.getItem("accessToken");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

// 응답 성공
api.interceptors.response.use(
  (res) => {
    useUIStore.getState().setGlobalLoading(false);
    return res;
  },
  (error) => {
    useUIStore.getState().setGlobalLoading(false);

    if (error.response?.status === 401) {
      console.warn("401 Unauthorized");
      // 향후 리프레시 토큰 처리 예정
      localStorage.removeItem("accessToken");
      window.location.href = "/login";
    }

    if (error.response?.status === 403) {
      console.warn("403 Forbidden");
    }

    return Promise.reject(error);
  }
);