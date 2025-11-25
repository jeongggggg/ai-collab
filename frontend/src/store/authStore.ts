import { create } from "zustand";
import { api } from "../api/client";

interface User {
  id: number;
  login: string;
  email: string;
  avatarUrl?: string;
}

interface AuthState {
  user: User | null;
  isLoading: boolean;
  fetchMe: () => Promise<void>;
  logout: () => void;
}

export const useAuthStore = create<AuthState>((set) => ({
  user: null,
  isLoading: true,

  fetchMe: async () => {
    try {
      const res = await api.get("/api/auth/me");
      set({ user: res.data, isLoading: false });
    } catch {
      set({ user: null, isLoading: false });
    }
  },

  logout: () => {
    localStorage.removeItem("accessToken");
    set({ user: null });
    window.location.href = "/login";
  },
}));